/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game_cuatro_en_linea.controlador;

import com.game_cuatro_en_linea.controlador.util.FacesUtils;
import com.game_cuatro_en_linea.controlador.util.JsfUtil;
import com.game_cuatro_en_linea.modelo.Usuario;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 *
 * @author Sebastian Henao
 */
@Named(value = "loginBean")
@ViewScoped
public class LoginBean implements Serializable{

    
    private Usuario usuario;
    @EJB
    private UsuarioFacade usuarioFacade; 

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public LoginBean() {        
    }
    
    @PostConstruct
    private void inicializar(){
        usuario= new Usuario();
    }
    
   /* private String ingresar(){
        
        Usuario usuarioEncontrado = usuarioFacade.find(usuario.getCorreo());
        if(usuarioEncontrado != null){
            if(usuario.getContrasena().compareTo(usuarioEncontrado.getContrasena())==0){
                ControladorCuatroEnLinea conCuatro= (ControladorCuatroEnLinea) FacesUtils.getManagedBean("controladorCuatroEnLinea");
                conCuatro.setUsuario(usuarioEncontrado);
                if(usuarioEncontrado.getTipoUsuario().getIdentificacion() == 0)
                {
                    return "ingresar";
                }    
                else
                {
                    return "jugar";
                }
            }
            JsfUtil.addErrorMessage("Contraseña errada");
        }
        else
        {
            JsfUtil.addErrorMessage("El correo ingresado no existe");
        }
        return null;        
        }*/
    
    public String ingresar() {
        Usuario usuarioEncontrado = usuarioFacade.findByEmail(usuario.getCorreo());
        if (usuarioEncontrado != null) {
            if (usuario.getContrasena().compareTo(usuarioEncontrado.getContrasena()) == 0) {
                if ("sebas@gmail.com".equals(usuario.getCorreo())){
                    return "ingresar";                
               }
                else if(usuario.getCorreo() != "sebas@gmai.com"){
                    return "jugar";
                }
                else JsfUtil.addErrorMessage("No tiene permisos para acceder");
                JsfUtil.addErrorMessage("Ingrese por el boton Jugar");

            }else  JsfUtil.addErrorMessage("Contraseña errada");
        } else {
            JsfUtil.addErrorMessage("El correo ingresado no existe");
        }
       
    
         return null;
    }

}

