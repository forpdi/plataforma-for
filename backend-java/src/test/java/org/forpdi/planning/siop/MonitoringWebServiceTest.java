package org.forpdi.planning.siop;

import java.security.SecureRandom;
import java.util.List;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.forpdi.system.factory.ApplicationSetup.DefaultTrustManager;
import org.junit.Assert;
import org.junit.Test;

import br.gov.planejamento.sof.siop.servicoweb.AcaoDTO;
import br.gov.planejamento.sof.siop.servicoweb.RetornoProgramacaoQualitativoDTO;

public class MonitoringWebServiceTest {

	@Test
	public void testWebServiceConnection() throws Exception {
		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(new KeyManager[0], new TrustManager[] { new DefaultTrustManager() }, new SecureRandom());
			SSLContext.setDefault(ctx);

			RetornoProgramacaoQualitativoDTO content = MonitoringWebService.accessWebService();
			Assert.assertNotNull("The response content is null.", content);
			if (!content.isSucesso()) {
				String msgs = "";
				for (String msg : content.getMensagensErro()) {
					msgs += msg + "\n";
				}
				Assert.fail(msgs);
			} else {
				List<AcaoDTO> acoes = content.getAcoesDTO();
				for (AcaoDTO acao : acoes) {
					System.out.printf("Ação: %s\n\t%s\n", acao.getTitulo(), acao.getUnidadeResponsavel());
				}
				
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
}
