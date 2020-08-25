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
@Table(name = "Editorial")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Editorial.findAll", query = "SELECT e FROM Editorial e")
    , @NamedQuery(name = "Editorial.findByIdEditorial", query = "SELECT e FROM Editorial e WHERE e.idEditorial = :idEditorial")
    , @NamedQuery(name = "Editorial.findByEdiNombre", query = "SELECT e FROM Editorial e WHERE e.ediNombre = :ediNombre")})
public class Editorial implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idEditorial")
    private Integer idEditorial;
    @Size(max = 45)
    @Column(name = "EdiNombre")
    private String ediNombre;

    public Editorial() {
    }

    public Editorial(Integer idEditorial) {
        this.idEditorial = idEditorial;
    }

    public Integer getIdEditorial() {
        return idEditorial;
    }

    public void setIdEditorial(Integer idEditorial) {
        this.idEditorial = idEditorial;
    }

    public String getEdiNombre() {
        return ediNombre;
    }

    public void setEdiNombre(String ediNombre) {
        this.ediNombre = ediNombre;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEditorial != null ? idEditorial.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Editorial)) {
            return false;
        }
        Editorial other = (Editorial) object;
        if ((this.idEditorial == null && other.idEditorial != null) || (this.idEditorial != null && !this.idEditorial.equals(other.idEditorial))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Nombre= "+ediNombre+"‎‎‎‎‎‎‎‎‎‎‏‏‎ ‎‏‏‎ ‎‏‏‎ ‎‏‏‎ ‎‏‏‎ ‎‏‏‎ ‎‏‏‎ ‎‏‏‎ ‎‏‏‎ ‎‏‏‎ ‎‏‏‎ ‎‏‏‎ ‎‏‏‎ ‎‏‏‎ ‎‏‏‎ ‎‏‏‎ ‎‏‏‎‏‏‎ ‎‏‏‎ ‎‏‏‎‏‏‎ ‎‏‏‎ ‎‏‏‎‏‏‎ ‎‏‏‎ ‎‏‏‎"+"id= "+idEditorial+"<hr>";
    }
    
}
