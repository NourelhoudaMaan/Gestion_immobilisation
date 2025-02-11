package controller;

import model.Materielsmar;
import controller.util.JsfUtil;
import controller.util.JsfUtil.PersistAction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@Named("materielsmarController")
@SessionScoped
public class MaterielsmarController implements Serializable {

    @EJB
    private controller.MaterielsmarFacade ejbFacade;
    private List<Materielsmar> items = null;
    private Materielsmar selected;

    public MaterielsmarController() {
    }

    public Materielsmar getSelected() {
        return selected;
    }

    public void setSelected(Materielsmar selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private MaterielsmarFacade getFacade() {
        return ejbFacade;
    }

    public Materielsmar prepareCreate() {
        selected = new Materielsmar();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/BundleMateriel").getString("MaterielsmarCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/BundleMateriel").getString("MaterielsmarUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/BundleMateriel").getString("MaterielsmarDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Materielsmar> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/BundleMateriel").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/BundleMateriel").getString("PersistenceErrorOccured"));
            }
        }
    }

    public Materielsmar getMaterielsmar(java.lang.Integer id) {
        return getFacade().find(id);
    }

    public List<Materielsmar> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Materielsmar> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Materielsmar.class)
    public static class MaterielsmarControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MaterielsmarController controller = (MaterielsmarController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "materielsmarController");
            return controller.getMaterielsmar(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Materielsmar) {
                Materielsmar o = (Materielsmar) object;
                return getStringKey(o.getIdM());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Materielsmar.class.getName()});
                return null;
            }
        }
    }
    
    public List<Materielsmar> listeDemandeMTL(){
        ArrayList<Materielsmar> liste = new ArrayList();
        List<Materielsmar>listeTest = ejbFacade.findAll();
        for(Materielsmar materiel : listeTest){
            if(materiel.getTypeM().equals("mtl")){
                liste.add(materiel);
            }
        }
        return liste ;
    }
    public List<Materielsmar> listeDemandeMGX(){
        ArrayList<Materielsmar> liste = new ArrayList();
        List<Materielsmar>listeTest = ejbFacade.findAll();
        for(Materielsmar materiel : listeTest){
            if(materiel.getTypeM().equals("mgx")){
                liste.add(materiel);
            }
        }
        return liste ;
    }
        
    private List<Materielsmar> filteredMaterielList;
    public List<Materielsmar> getFilteredMaterielList() {
        return filteredMaterielList;
    }
        
    public void setFilteredMaterielList(List<Materielsmar> filteredMaterielList) {
        this.filteredMaterielList = filteredMaterielList;
    }

}
