package br.com.caelum.financas.service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.ScheduleExpression;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

@Stateless
public class Agendador {

	private static int totalCriado;
	@Resource
	private TimerService timerService;
	
	public void executa() {
		System.out.printf("%d instancias criadas %n", totalCriado);

		// simulando demora de 4s na execucao
		try {
			System.out.printf("Executando %s %n", this);
			Thread.sleep(4000);
		} catch (InterruptedException e) {
		}
	}
	
	@PostConstruct
	public void postConstruct()
	{
			System.out.println("Criando Agendador");
			totalCriado++;
	}
	
	@PreDestroy
	public void preDestroy()
	{
		System.out.println("Destruindo agendador");
	}
	
	
	public void agenda(String expressaoMinutos, String expressaoSegundos)
	{
		ScheduleExpression expression = new ScheduleExpression();
		expression.hour("*");
		expression.minute(expressaoMinutos);
		expression.second(expressaoSegundos);
		
		TimerConfig config = new TimerConfig();
		config.setInfo(expression.toString());
		config.setPersistent(false);
		
		this.timerService.createCalendarTimer(expression, config);
		
		System.out.println("Agendamento : "+expression);
		
		
	}
	
	@Timeout
	public void verificacaoPeriodoSeNaNovasContas(Timer timer)
	{
		System.out.println(timer.getInfo());
	}
}
