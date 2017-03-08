package org.forpdi.planning.siop;

import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import br.gov.planejamento.sof.siop.servicoweb.CredencialDTO;
import br.gov.planejamento.sof.siop.servicoweb.RetornoMomentoDTO;
import br.gov.planejamento.sof.siop.servicoweb.RetornoProgramacaoQualitativoDTO;
import br.gov.planejamento.sof.siop.servicoweb.WSQualitativo;


public class MonitoringWebService {
	
	public static RetornoProgramacaoQualitativoDTO accessWebService() throws Exception {
		final CredencialDTO credencial = new CredencialDTO();
		credencial.setUsuario("WS-FORPDI-UNIFAL-MG");
		credencial.setSenha("278cf6fbb8f7eaadbb4d9b0038bc5dcf");
		credencial.setPerfil(31);
		
		URL url = new URL("https://testews.siop.gov.br/services/WSQualitativo?wsdl");
        QName qname = new QName("http://servicoweb.siop.sof.planejamento.gov.br/","WSQualitativo");
        Service ws = Service.create(url, qname);
        WSQualitativo qualitativo = ws.getPort(WSQualitativo.class);
        RetornoMomentoDTO momento = qualitativo.obterMomentoCarga(credencial, 2016);
        RetornoProgramacaoQualitativoDTO programacao = qualitativo.obterProgramacaoCompleta(credencial, 2016,
        		momento.getMomento().getCodigoMomento(),
        		false, false, false, false, false, true, false, false, false, false, false, false, null);
        return programacao;
	}
}
