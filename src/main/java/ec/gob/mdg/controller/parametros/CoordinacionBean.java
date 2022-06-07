package ec.gob.mdg.controller.parametros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Coordinacion;
import ec.gob.mdg.control.ejb.service.ICoordinacionService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class CoordinacionBean implements Serializable {

	private static final long serialVersionUID = -2622304613361080515L;

	@Inject
	private ICoordinacionService serviceCoordinacion;

	private List<Coordinacion> listaCoordinacion = new ArrayList<Coordinacion>();

	private Coordinacion coordinacion = new Coordinacion();

	private String tipoDialog = null;

	@PostConstruct
	public void init() {
		listarCoordinacion();
		this.tipoDialog = "Nuevo";
	}

	public void listarCoordinacion() {
		try {
			this.listaCoordinacion = this.serviceCoordinacion.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void operar(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				this.serviceCoordinacion.registrar(coordinacion);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceCoordinacion.modificar(coordinacion);
			}
			this.listarCoordinacion();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(Coordinacion i) {
		this.coordinacion = i;
		this.tipoDialog = "Modificar Coordinación";
	}

	public void limpiarControles() {
		this.coordinacion = new Coordinacion();
		this.tipoDialog = "Nueva Coordinación";
	}
	
}
