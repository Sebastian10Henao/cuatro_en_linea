/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game_cuatro_en_linea.controlador;

import com.game_cuatro_en_linea.modelo.Usuario;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Sebastian Henao
 */
@Stateless
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext(unitName = "GameCuatroEnLineaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsuarioFacade() {
        super(Usuario.class);
    }
    
    public Usuario findByEmail(String email)
    {
        Query q = em.createNamedQuery("Usuario.findByCorreo", Usuario.class).setParameter("correo", email);
        
        List list= q.getResultList();
        
        if(list.isEmpty())
        {
            return null;
        }
        return (Usuario) list.get(0);
    }
    
}
