package ec.gob.mdg.controller.entidades.calificacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ConsultaCalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;
	private List<Empresa> listaEmpresas = new ArrayList<Empresa>();
	private Empresa empresa = new Empresa();

	String nombre_emp;
	String id_emp;
	String estado="ingresado";

	public void consultarListaEmpresas(String nombre) {
		String estado="ingresado";
		if (nombre != null) {
			listaEmpresas = this.serviceEmpresa.listarEmpresasPorNombreEstado(nombre.toUpperCase(),estado);
		}
	}

	/// Ir a detalle de empresa
	public void irDetalleEmpresa(String codigo) {
		if (codigo!=null) {
			final FacesContext context = FacesContext.getCurrentInstance();
			final Flash flash = context.getExternalContext().getFlash();
			flash.put("empresa", codigo);
			Utilitario.irAPagina("/pg/cal/entprincipalcal");
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"No hay datos o No puede regresar", "Sin datos"));
		}
		
	}
	

}
