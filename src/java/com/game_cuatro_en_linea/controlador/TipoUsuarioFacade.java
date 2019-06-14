/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game_cuatro_en_linea.controlador;

import com.game_cuatro_en_linea.modelo.TipoUsuario;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sebastian Henao
 */
@Stateless
public class TipoUsuarioFacade extends AbstractFacade<TipoUsuario> {

    @PersistenceContext(unitName = "GameCuatroEnLineaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TipoUsuarioFacade() {
        super(TipoUsuario.class);
    }
    
}
