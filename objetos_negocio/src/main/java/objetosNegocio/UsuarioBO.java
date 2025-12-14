package objetosNegocio;

public class UsuarioBO implements IUsuarioBO {
    private String id;
    private String nombre;
    private String email;

    public UsuarioBO() {}

    public UsuarioBO(String id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

