import moment from 'moment';
import React from 'react';



export default React.createClass({
	getDefaultProps() {
		return {

		};
	},

	getInitialState() {
		return {
		};
	},

	componentDidMount(){ 

	},

	componentWillUnmount() {

	},

	
	
	render() {
	
			return (

				<div className="fpdi-ToolTipNotification">	

					 <div className='displayFlex-goals'>
					 	<h3 id = "TitleSubtitleNotification"> O PDI  </h3>
  	            	</div>

  	            	<div className='displayFlex-goals'>
					 		<p id = "TitleSubtitleNotification">
					 			O Plano de Desenvolvimento Institucional (PDI) é o instrumento de planejamento e gestão obrigatório para todas as instituições de ensino superior. 
					 			O PDI consiste num documento em que se definem a missão da instituição de ensino superior e as ações para atingir suas metas e objetivos de curto e longo prazo. 
					 			Ele é elaborado periodicamente, geralmente a cada 4 ou 5 anos, a partir dos resultados de uma avaliação institucional do passado e presente, para se seja definido 
					 			um planejamento com foco no futuro, trançando diretrizes e estratégias para o desenvolvimento da instituição.
					 		</p>
					 		<p id = "TitleSubtitleNotification">
					 			O painel de bordo da comunidade torna públicos os resultados do PDI para toda a comunidade acadêmica, fomentando os objetivos de eficiência, 
					 			eficácia e transparência sobre as informações e as ações desenvolvidas pela administração pública.
					 		</p>
					 </div>
  	          </div>			
			);
		
	}
});