/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game_cuatro_en_linea.controlador;

import com.game_cuatro_en_linea.modelo.Ficha;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Sebastian Henao
 */
@Stateless
public class FichaFacade extends AbstractFacade<Ficha> {

    @PersistenceContext(unitName = "GameCuatroEnLineaPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public FichaFacade() {
        super(Ficha.class);
    }
    
}
