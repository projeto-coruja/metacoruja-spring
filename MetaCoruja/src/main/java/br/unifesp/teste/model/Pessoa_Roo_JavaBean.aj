// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package br.unifesp.teste.model;

import br.unifesp.teste.model.Pessoa;

privileged aspect Pessoa_Roo_JavaBean {
    
    public String Pessoa.getNome() {
        return this.Nome;
    }
    
    public void Pessoa.setNome(String Nome) {
        this.Nome = Nome;
    }
    
    public Boolean Pessoa.getOnline() {
        return this.Online;
    }
    
    public void Pessoa.setOnline(Boolean Online) {
        this.Online = Online;
    }
    
}
