/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entidad;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author andre
 */
@Entity
@Table(name = "Libro")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Libro.findAll", query = "SELECT l FROM Libro l")
    , @NamedQuery(name = "Libro.findById", query = "SELECT l FROM Libro l WHERE l.id = :id")
    , @NamedQuery(name = "Libro.findByLibTitulo", query = "SELECT l FROM Libro l WHERE l.libTitulo = :libTitulo")
    , @NamedQuery(name = "Libro.findByLibPrecio", query = "SELECT l FROM Libro l WHERE l.libPrecio = :libPrecio")
    , @NamedQuery(name = "Libro.findByLibGenero", query = "SELECT l FROM Libro l WHERE l.libGenero = :libGenero")
    , @NamedQuery(name = "Libro.findByLibStock", query = "SELECT l FROM Libro l WHERE l.libStock = :libStock")})
public class Libro implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "Id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "LibTitulo")
    private String libTitulo;
    @Column(name = "LibPrecio")
    private Integer libPrecio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "LibGenero")
    private String libGenero;
    @Basic(optional = false)
    @NotNull
    @Column(name = "LibStock")
    private short libStock;

    public Libro() {
    }

    public Libro(Integer id) {
        this.id = id;
    }

    public Libro(Integer id, String libTitulo, String libGenero, short libStock) {
        this.id = id;
        this.libTitulo = libTitulo;
        this.libGenero = libGenero;
        this.libStock = libStock;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibTitulo() {
        return libTitulo;
    }

    public void setLibTitulo(String libTitulo) {
        this.libTitulo = libTitulo;
    }

    public Integer getLibPrecio() {
        return libPrecio;
    }

    public void setLibPrecio(Integer libPrecio) {
        this.libPrecio = libPrecio;
    }

    public String getLibGenero() {
        return libGenero;
    }

    public void setLibGenero(String libGenero) {
        this.libGenero = libGenero;
    }

    public short getLibStock() {
        return libStock;
    }

    public void setLibStock(short libStock) {
        this.libStock = libStock;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Libro)) {
            return false;
        }
        Libro other = (Libro) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
    
}
