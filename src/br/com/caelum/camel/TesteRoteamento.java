package br.com.caelum.camel;

import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.processor.DeadLetterChannel;

public class TesteRoteamento {
	
	public static void main(String[] args) throws Exception {
		
		CamelContext ctx = new DefaultCamelContext();
		ctx.addRoutes(new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				
				onException(RuntimeException.class).
					log("Excecao processando ${file:name}").
						handled(true).
							to("file:exception");
				
				errorHandler (
					deadLetterChannel("file:falha").
					maximumRedeliveries(2).
					redeliveryDelay(2000)
				);
				from("file:entrada?delay=5s").
				log(LoggingLevel.INFO, "Processando mensagem ${id}" ).
				bean(ValidadorPedido.class, "validar").
				transform(body(String.class).regexReplaceAll("nomeAutor", "autor")).
				to("file:saida");
				
			}
		});
		
		ctx.start();
		Thread.sleep(30*1000);
		ctx.stop();
	}

}
