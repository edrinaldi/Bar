package it.polito.tdp.bar.model;

public class Model {

	private Simulator sim;
	
	public Model() {
		this.sim = new Simulator();
	}
	
	public void simula() {
		this.sim.init();
		this.sim.run();
	}
	
	public int getClienti() {
		return this.sim.getClienti();
	}
	
	public int getSoddisfatti() {
		return this.sim.getSoddisfatti();
	}
	
	public int getInsoddisfatti() {
		return this.sim.getInsoddisfatti();
	}
}
