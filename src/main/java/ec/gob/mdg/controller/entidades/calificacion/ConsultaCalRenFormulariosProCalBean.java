package ec.gob.mdg.controller.entidades.calificacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import ec.gob.mdg.control.ejb.modelo.CalificacionesRenovaciones;
import ec.gob.mdg.control.ejb.modelo.CalrenActividadesCalificacion;
import ec.gob.mdg.control.ejb.modelo.CalrenSustancias;
import ec.gob.mdg.control.ejb.modelo.CalrenSustanciasActividades;
import ec.gob.mdg.control.ejb.modelo.CalrenSustanciasActividadesProRecReu;
import ec.gob.mdg.control.ejb.modelo.CalrenSustanciasActividadesProRecReuMateriaPrima;
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.service.ICalificacionesRenovacionesService;
import ec.gob.mdg.control.ejb.service.ICalrenActividadesCalificacionService;
import ec.gob.mdg.control.ejb.service.ICalrenSustanciasActividadesProRecReuMateriaPrimaService;
import ec.gob.mdg.control.ejb.service.ICalrenSustanciasActividadesProRecReuService;
import ec.gob.mdg.control.ejb.service.ICalrenSustanciasActividadesService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@SessionScoped
public class ConsultaCalRenFormulariosProCalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;

	@Inject
	private ICalificacionesRenovacionesService serviceCalRen;

	@Inject
	private ICalrenSustanciasActividadesService serviceCalRenSusAct;

	@Inject
	private ICalrenActividadesCalificacionService serviceCalRenActCal;

	@Inject
	private ICalrenSustanciasActividadesProRecReuService serviceCalrenSusActProRecReu;

	@Inject
	private ICalrenSustanciasActividadesProRecReuMateriaPrimaService serviceCalrenSusActProRecReuMateriaPrima;

	private List<CalrenSustancias> listaCalRenSustancias = new ArrayList<>();
	private List<CalrenSustanciasActividades> listaCalRenSustanciasAct = new ArrayList<>();
	private List<CalrenSustanciasActividadesProRecReu> listaCalRenSusActProRecReu = new ArrayList<>();
	private List<CalrenSustanciasActividadesProRecReuMateriaPrima> listaCalRenSusActProRecReuMateriaPrima = new ArrayList<>();

	private Empresa empresa = new Empresa();
	private CalificacionesRenovaciones calRen = new CalificacionesRenovaciones();
	private CalrenSustanciasActividades calrenSustanciasActividades = new CalrenSustanciasActividades();
	private CalrenActividadesCalificacion calRenActCal = new CalrenActividadesCalificacion();
	private CalrenSustanciasActividadesProRecReu calRenSusActProRecReu = new CalrenSustanciasActividadesProRecReu();
	private CalrenSustanciasActividadesProRecReuMateriaPrima calRenSusActMateriaPrima = new CalrenSustanciasActividadesProRecReuMateriaPrima();

	String calrenactS;
	Integer calrenactId;
	String abreviatura;
	String calrenS;
	Integer calrenId;

	@PostConstruct
	public void init() {
		try {
			cargarDatos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("calrenact");
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public void cargarDatos() {
		abreviatura = "P";
		calrenactS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("calrenact");
		calrenactId = Integer.parseInt(calrenactS);
		if (calrenactId != null) {
			calRenActCal = serviceCalRenActCal.listaCalrenActividadesId(calrenactId);

			if (calRenActCal != null) {
				calRen = serviceCalRen.calrenPorId(calRenActCal.getCalificacionesRenovaciones().getId());

				if (calRen != null) {
					listaCalRenSustanciasAct = serviceCalRenSusAct.listaSustActiPorAbreviatura(calRen.getId(),
							abreviatura);
					empresa = serviceEmpresa.listarEmpresaPorId(calRen.getEmpresa().getId());

					if (listaCalRenSustanciasAct != null && !listaCalRenSustanciasAct.isEmpty()) {
						calrenSustanciasActividades = listaCalRenSustanciasAct.get(0);
						if (calrenSustanciasActividades != null) {
							cargarListaCapacidad(calrenSustanciasActividades.getId());
							if (listaCalRenSusActProRecReu != null && !listaCalRenSusActProRecReu.isEmpty()) {
								calRenSusActProRecReu = listaCalRenSusActProRecReu.get(0);
								if (calRenSusActProRecReu != null) {
									cargarListaMateriaPrima(calRenSusActProRecReu.getId());
								}
							}
						}
					}
				}
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "No hay datos", "No puede continuar"));
		}
	}

	public void cargarListaCapacidad(Integer id_CalRenSusAct) {
		String tipo_actividad = "P";
		if (id_CalRenSusAct != null) {
			listaCalRenSusActProRecReu = serviceCalrenSusActProRecReu.listarCalrenActividadesProRecReu(id_CalRenSusAct, tipo_actividad);
		}
	}

	public void cargarListaMateriaPrima(Integer id_CalRenSusActProRecReu) {
		if (id_CalRenSusActProRecReu != null) {
			listaCalRenSusActProRecReuMateriaPrima = serviceCalrenSusActProRecReuMateriaPrima
					.listarCalrenActividadesMateriaPrima(id_CalRenSusActProRecReu);
		}
	}


	public void onRowSelect(SelectEvent<CalrenSustanciasActividadesProRecReu> event) {
		cargarListaMateriaPrima(((CalrenSustanciasActividadesProRecReu) event.getObject()).getId());
	}

	public void onRowUnselect(UnselectEvent<CalrenSustanciasActividadesProRecReu> event) {
		cargarListaMateriaPrima(((CalrenSustanciasActividadesProRecReu) event.getObject()).getId());
	}

	/// Ir a Formularios actividades
	public void irFormularios() {
		if (calRen.getId() != null) {
			calrenS = String.valueOf(calRen.getId());
			final FacesContext context = FacesContext.getCurrentInstance();
			final Flash flash = context.getExternalContext().getFlash();
			flash.put("calren", calrenS);
			Utilitario.irAPagina("/pg/cal/calrenformulariosactcal");
		}
	}

}
