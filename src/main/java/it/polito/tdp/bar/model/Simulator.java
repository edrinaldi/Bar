package it.polito.tdp.bar.model;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import it.polito.tdp.bar.model.Event.EventType;

public class Simulator {

	// Modello
	private List<Tavolo> tavoli;
	
	// Parametri della simulazione
	private int NUM_EVENTI = 2000;
	private int T_ARRIVO_MAX = 10;
	private int NUM_PERSONE_MAX = 10;
	private int DURATA_MIN = 60;
	private int DURATA_MAX = 120;
	private double TOLLERANZA_MAX = 0.9;
	private double OCCUPAZIONE_MAX = 0.5;
	
	// Coda degli eventi
	private PriorityQueue<Event> queue;
	
	// Statistiche (parametri in uscita)
	private Statistiche statistiche;
	
	public void init() {
		this.queue = new PriorityQueue<>();
		this.tavoli = new ArrayList<>();
		this.statistiche = new Statistiche();
		
		this.creaTavoli();
		this.creaEventi();
	}

	private void creaEventi() {
		// TODO Auto-generated method stub
		Duration arrivo = Duration.ofMinutes(0);
		for(int i=0; i<this.NUM_EVENTI; i++) {
			int nPersone = (int)(Math.random()*this.NUM_PERSONE_MAX+1);
			Duration durata = Duration.ofMinutes(this.DURATA_MIN+
					(int)(Math.random()*(this.DURATA_MAX-this.DURATA_MIN+1)));
			double tolleranza = Math.random()*this.TOLLERANZA_MAX;
			Event e = new Event(EventType.ARRIVO_GRUPPO_CLIENTI, arrivo, nPersone,
					durata, tolleranza, null);
			this.queue.add(e);
			arrivo = arrivo.plusMinutes((int)(Math.random()*this.T_ARRIVO_MAX+1));
		}
	}

	private void creaTavoli() {
		// TODO Auto-generated method stub
		this.creaTavolo(2, 10);
		this.creaTavolo(4, 8);
		this.creaTavolo(4, 6);
		this.creaTavolo(5, 4);
		
		Collections.sort(this.tavoli, new Comparator<Tavolo>() {

			@Override
			public int compare(Tavolo o1, Tavolo o2) {
				// TODO Auto-generated method stub
				return o1.getPosti() - o2.getPosti();
			}
			
		});
	}

	private void creaTavolo(int quantità, int dimensione) {
		// TODO Auto-generated method stub
		for(int i=0; i<quantità; i++) {
			this.tavoli.add(new Tavolo(dimensione, false));
		}
	}
	
	public void run() {
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			this.processaEvento(e);
		}
	}

	private void processaEvento(Event e) {
		// TODO Auto-generated method stub
		switch(e.getType()) {
			case ARRIVO_GRUPPO_CLIENTI:
				// conto i clienti tot
				this.statistiche.incrementaClienti(e.getnPersone());
				
				// cerco un tavolo (che sia il piu' piccolo e t.c. ne venga occupata 
				// almeno meta')
				Tavolo tavolo = null;
				for(Tavolo t : this.tavoli) {
					if(!t.isOccupato() && t.getPosti() >= e.getnPersone() &&
							t.getPosti()*this.OCCUPAZIONE_MAX <= e.getnPersone()) {
						// è sicuramente il piu' piccolo perche' la lista di tavoli e'
						// appositamente ordinata per numero di posti
						tavolo = t;
						break;
					}
				}
				
				if(tavolo != null) {
					System.out.format("Trovato un tavolo da %d per %d persone.\n", 
							tavolo.getPosti(), e.getnPersone());
					this.statistiche.incrementaSoddisfatti(e.getnPersone());
					tavolo.setOccupato(true);
					e.setTavolo(tavolo);	// inutile
					
					// dopo un po' i clienti si alzeranno
					this.queue.add(new Event(EventType.TAVOLO_LIBERATO, 
							e.getTime().plus(e.getDurata()), e.getnPersone(),
							e.getDurata(), e.getTolleranza(), tavolo));
				}
				else {
					// c'e' solo il bancone, valuto la tolleranza
					double bancone = Math.random();
					if(bancone <= e.getTolleranza()) {
						// si fermano
						System.out.format("%d persone si fermano al bancone.\n", 
								e.getnPersone());
						this.statistiche.incrementaSoddisfatti(e.getnPersone());
					}
					else {
						// vanno a casa
						System.out.format("%d persone vanno a casa.\n", 
								e.getnPersone());
						this.statistiche.incrementaInsoddisfatti(e.getnPersone());
					}
				}
				
				break;
			case TAVOLO_LIBERATO:
				e.getTavolo().setOccupato(false);
				break;
		}
	}

	public int getClienti() {
		// TODO Auto-generated method stub
		return this.statistiche.getClientiTot();
	}

	public int getSoddisfatti() {
		// TODO Auto-generated method stub
		return this.statistiche.getClientiSoddisfatti();
	}
	
	public int getInsoddisfatti() {
		// TODO Auto-generated method stub
		return this.statistiche.getClientiInsoddisfatti();
	}
}
