package br.com.caelum.camel;

import org.apache.camel.Exchange;

public class ValidadorPedido {
	
	public void validar(Exchange exc) {
		System.out.println("Validando: "+ exc.getExchangeId());
		String xml = exc.getIn().getBody(String.class);
		
		if (!xml.contains("<pagamento>")) {
			throw new RuntimeException("Sem pagamento no pedido");
		}
	}

}
