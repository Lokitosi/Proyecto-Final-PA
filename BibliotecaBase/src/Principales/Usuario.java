package Principales;


public class Usuario {
    String nombre,password,email,ultimoLibro;
    
    public Usuario(String n,String p,String em, String ul){
    this.nombre = n;
    this.password = p;
    this.email = em;
    this.ultimoLibro = ul;
}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUltimoLibro() {
        return ultimoLibro;
    }

    public void setUltimoLibro(String ultimoLibro) {
        this.ultimoLibro = ultimoLibro;
    }
    
    
}
