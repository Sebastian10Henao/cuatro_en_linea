/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game_cuatro_en_linea.controlador;

import com.game_cuatro_en_linea.controlador.util.JsfUtil;
//import com.cuatro_en_linea.modelo.Ficha;
import com.game_cuatro_en_linea.modelo.Usuario;
import com.game_cuatro_en_linea.modelo.grafo.Ficha;
import com.game_cuatro_en_linea.modelo.grafo.Arista;
import com.game_cuatro_en_linea.modelo.grafo.Grafo;
import com.game_cuatro_en_linea.modelo.grafo.Vertice;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.StraightConnector;
import org.primefaces.model.diagram.endpoint.BlankEndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;

/**
 *
 * @author Sebastian Henao
 */
@Named(value = "controladorCuatroEnLinea")
@ApplicationScoped
public class ControladorGameCuatroEnLinea implements Serializable {

    /*private Usuario usuario;
    @EJB   
    private UsuarioFacade usuarioFacade;    
    @EJB*/
    private int ancho = 7;
    private int alto = ancho - 1;
    private int total = alto * ancho;
    private DefaultDiagramModel model;
    
    private Grafo tablero = new Grafo();
    
    private String fichaClick;
    private Vertice vertice;
    private boolean estadoJuego;
    private Date fechaSistema;

    private List<Vertice> vertices;
    private int numeroEscalera;

    public int getNumeroEscalera() {
        return numeroEscalera;
    }

    public void setNumeroEscalera(int numeroEscalera) {
        this.numeroEscalera = numeroEscalera;
    }

    public Date getFechaSistema() {
        return new Date();
    }

    public void setFechaSistema(Date fechaSistema) {
        this.fechaSistema = fechaSistema;
    }

    public boolean isEstadoJuego() {
        return estadoJuego;
    }

    public void setEstadoJuego(boolean estadoJuego) {
        this.estadoJuego = estadoJuego;
    }

    public String getFichaClick() {
        return fichaClick;
    }

    public void setFichaClick(String fichaClick) {
        this.fichaClick = fichaClick;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    /*public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }*/
    /**
     * Creates a new instance of ControladorCuatroEnLinea
     */
    public ControladorGameCuatroEnLinea() {
    }

    public void llenarAristas() {

        //Crear aristas
        for (int i = 1; i <= 6; i++) {
            for (Vertice vert : tablero.obtenerVerticesTablero("T" + i)) {
                if (vert.getId() % ancho != 0) {
                    tablero.adicionarArista(vert.getId(), vert.getId() + 1, 2);
                }
                if (vert.getId() + ancho <= (total * i)) {
                    tablero.adicionarArista(vert.getId(), vert.getId() + ancho, 1);

                    if (vert.getId() % ancho != 0) {
                        tablero.adicionarArista(vert.getId(), vert.getId() + ancho + 1, 1);
                        tablero.adicionarArista(vert.getId() + 1, vert.getId() + ancho, 1);
                    }
                }

            }
        }
    }

    @PostConstruct
    public void pintarTablero() {

        model = new DefaultDiagramModel();
        model.setMaxConnections(-1);
        model.setConnectionsDetachable(false);

        int posc = 2;
        for (int t = 0; t < 6; t++) {
            int x = posc;
            int y = 5;
            String color = "Rojo";
            String styleColor = "ui-diagram-element-ficha-gris";
            for (int i = 1; i <= alto; i++) {

                for (int j = 1; j <= ancho; j++) {

                    tablero.adicionarVertice(new Ficha(color, "T" + (t + 1)));
                    Element ceo = new Element(tablero.getVertices().size(), x + "em", y + "em");
                    ceo.setId(String.valueOf(tablero.getVertices().size()));
                    ceo.addEndPoint(new BlankEndPoint(EndPointAnchor.BOTTOM));
                    ceo.addEndPoint(new BlankEndPoint(EndPointAnchor.TOP));
                    ceo.addEndPoint(new BlankEndPoint(EndPointAnchor.LEFT));
                    ceo.addEndPoint(new BlankEndPoint(EndPointAnchor.RIGHT));

                    ceo.setDraggable(false);
                    ceo.setStyleClass(styleColor);
                    ceo.addEndPoint(new BlankEndPoint(EndPointAnchor.CENTER));
                    model.addElement(ceo);
                    x = x + 5;
                }
                y = y + 5;
                x = posc;
            }
            posc = posc + 40;
        }

        llenarAristas();
        StraightConnector connector = new StraightConnector();
        connector.setPaintStyle("{strokeStyle:'#404a4e', lineWidth:3}");
        connector.setHoverPaintStyle("{strokeStyle:'#20282b'}");
        model.setDefaultConnector(connector);

        //recorrer aristas
        for (Arista arista : tablero.getAristas()) {
            Element origen = model.getElements().get(arista.getOrigen() - 1);
            Element destino = model.getElements().get(arista.getDestino() - 1);

            switch (arista.getPeso()) {
                case 1:
                    model.connect(new Connection(origen.getEndPoints().get(0), destino.getEndPoints().get(1)));
                    break;
                case 2:
                    model.connect(new Connection(origen.getEndPoints().get(3), destino.getEndPoints().get(2)));
                    break;
            }
        }
    }

    public void escalera() {
        
        for (Vertice vert : tablero.getVertices()) {
            if(vert.getId() >= numeroEscalera && vert.getId() - 1 == ancho){
                //String styleColor = "ui-diagram-element-ficha-azul";
                //model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
                for(int i = vert.getId(); i % ancho <= 0; i++){
                    String styleColor = "ui-diagram-element-ficha-azul";
                    model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
                }
            }
            else{
                String styleColor = "ui-diagram-element-ficha-gris";
                model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
            }




//            if(vert.getId() >= numeroEscalera && (vert.getId() -1) - numeroEscalera <= ancho){
//                //for(int i = vert.getId(); i - 1 % ancho != 0; i++){
//                    String styleColor = "ui-diagram-element-ficha-azul";
//                    model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
//                    
//                //}
//            }
//            else{
//                String styleColor = "ui-diagram-element-ficha-gris";
//                model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
//            }
            
            
//                if(vert.getId() >= numeroEscalera){
//                    for (int i = vert.getId() ; i <= total; i++) {
//                        if(i % ancho != 0){
//                            String styleColor = "ui-diagram-element-ficha-azul";
//                            model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
//                        }
//                }
//            }

        }
        
        
        
        
//        for (Vertice vert : tablero.getVertices()) {
//            
//                for(int i = vert.getId(); i == total; i++ ){
//                    
//                    if(vert.getId() == numeroEscalera){
//                        String styleColor = "ui-diagram-element-ficha-azul";
//                        model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
//                    }
//                    
//                    if(vert.getId() - 1 % ancho != 0){
//                        String styleColor = "ui-diagram-element-ficha-azul";
//                        model.getElements().get(vert.getId() + 1).setStyleClass(styleColor);
//                    }
//             }
//            
//            for(int i = vert.getId(); i == ancho; i++){
//                if(i == numeroEscaler a){
//                    String styleColor = "ui-diagram-element-ficha-azul";
//                    model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
//                }
//                else{
//                    String styleColor = "ui-diagram-element-ficha-gris";
//                    model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
//                }
//            }
            
//            if (vert.getId() >= numeroEscalera) {    
//                String styleColor = "ui-diagram-element-ficha-azul";
//                 model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
//                 
//                for(int i= vert.getId()-1; i < ancho; i++){
//                    if(vert.getId() % ancho != 0){
//                        styleColor = "ui-diagram-element-ficha-azul";
//                        model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
//                    }else{
//                        styleColor = "ui-diagram-element-ficha-gris";
//                        model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
//                    }
//            }
                
//                if((vert.getId() + 1) % ancho != 0){
//                    String styleColor = "ui-diagram-element-ficha-azul";
//                    model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
//                }else{
//                    String styleColor = "ui-diagram-element-ficha-gris";
//                    model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
//                }                 
             
           // }
            
            
            
//            if(numeroEscalera <= total){
//                String styleColor = "ui-diagram-element-ficha-azul";
//                model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
//            }
            
//            if (vert.getId() == numeroEscalera) {
//                String styleColor = "ui-diagram-element-ficha-azul";
//                model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
//            }
//            for(int i = numeroEscalera; i % ancho == 0; i++){
//                String styleColor = "ui-diagram-element-ficha-azul";
//                model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
//            }
            
//            else{
//                String styleColor = "ui-diagram-element-ficha-gris";
//                model.getElements().get(vert.getId() - 1).setStyleClass(styleColor);
//        }
        //}
    }

    public void activarJuego() {
        estadoJuego = true;
        JsfUtil.addSuccessMessage("Se ha habilitado el juego");
    }

    /*public void jugar(int Ficha){
       int pos = ;
        Element ficha = model.getElements().get(pos);
        while(pos <= total){
            
        }
    }*/
    public void onClickRight() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("elementId");

        fichaClick = id.replaceAll("frmTablero:tablero-", "");
    }

    public void pruebaMenu() {
        JsfUtil.addSuccessMessage(fichaClick + " presionada");
    }

    public int getAncho() {
        return ancho;
    }

    public void setAncho(int ancho) {
        this.ancho = ancho;
    }

    public int getAlto() {
        return alto;
    }

    public void setAlto(int alto) {
        this.alto = alto;
    }

    public DefaultDiagramModel getModel() {
        return model;
    }

    public void setModel(DefaultDiagramModel model) {
        this.model = model;
    }

    public Grafo getTablero() {
        return tablero;
    }

    public void setTablero(Grafo tablero) {
        this.tablero = tablero;
    }

    /*void setUsuario(Usuario usuarioEncontrado) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/
}
