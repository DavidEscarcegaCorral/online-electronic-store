package objetosnegocio;

public class ConfiguracionBO {
    private Long id;
    private Componente procesador;
    private Componente tarjetaMadre;
    private Componente memoriaRAM;
    private Componente almacenamiento;
    private Componente unidadSSD;
    private Componente tarjetaDeVideo;
    private Componente fuenteDePoder;
    private Componente disipador;
    private Componente ventiladore;
    private Componente gabinete;
    private Componente monitor;
    private Componente kitTecladoRaton;
    private Componente red;

    public ConfiguracionBO() {

    }


    public ConfiguracionBO(Long id, Componente procesador, Componente tarjetaMadre, Componente memoriaRAM,
                           Componente almacenamiento, Componente unidadSSD, Componente tarjetaDeVideo,
                           Componente fuenteDePoder, Componente disipador, Componente ventiladore,
                           Componente gabinete, Componente monitor, Componente kitTecladoRaton, Componente red) {
        this.id = id;
        this.procesador = procesador;
        this.tarjetaMadre = tarjetaMadre;
        this.memoriaRAM = memoriaRAM;
        this.almacenamiento = almacenamiento;
        this.unidadSSD = unidadSSD;
        this.tarjetaDeVideo = tarjetaDeVideo;
        this.fuenteDePoder = fuenteDePoder;
        this.disipador = disipador;
        this.ventiladore = ventiladore;
        this.gabinete = gabinete;
        this.monitor = monitor;
        this.kitTecladoRaton = kitTecladoRaton;
        this.red = red;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Componente getProcesador() {
        return procesador;
    }

    public void setProcesador(Componente procesador) {
        this.procesador = procesador;
    }

    public Componente getTarjetaMadre() {
        return tarjetaMadre;
    }

    public void setTarjetaMadre(Componente tarjetaMadre) {
        this.tarjetaMadre = tarjetaMadre;
    }

    public Componente getMemoriaRAM() {
        return memoriaRAM;
    }

    public void setMemoriaRAM(Componente memoriaRAM) {
        this.memoriaRAM = memoriaRAM;
    }

    public Componente getAlmacenamiento() {
        return almacenamiento;
    }

    public void setAlmacenamiento(Componente almacenamiento) {
        this.almacenamiento = almacenamiento;
    }

    public Componente getUnidadSSD() {
        return unidadSSD;
    }

    public void setUnidadSSD(Componente unidadSSD) {
        this.unidadSSD = unidadSSD;
    }

    public Componente getTarjetaDeVideo() {
        return tarjetaDeVideo;
    }

    public void setTarjetaDeVideo(Componente tarjetaDeVideo) {
        this.tarjetaDeVideo = tarjetaDeVideo;
    }

    public Componente getFuenteDePoder() {
        return fuenteDePoder;
    }

    public void setFuenteDePoder(Componente fuenteDePoder) {
        this.fuenteDePoder = fuenteDePoder;
    }

    public Componente getDisipador() {
        return disipador;
    }

    public void setDisipador(Componente disipador) {
        this.disipador = disipador;
    }

    public Componente getVentiladore() {
        return ventiladore;
    }

    public void setVentiladore(Componente ventiladore) {
        this.ventiladore = ventiladore;
    }

    public Componente getGabinete() {
        return gabinete;
    }

    public void setGabinete(Componente gabinete) {
        this.gabinete = gabinete;
    }

    public Componente getMonitor() {
        return monitor;
    }

    public void setMonitor(Componente monitor) {
        this.monitor = monitor;
    }

    public Componente getKitTecladoRaton() {
        return kitTecladoRaton;
    }

    public void setKitTecladoRaton(Componente kitTecladoRaton) {
        this.kitTecladoRaton = kitTecladoRaton;
    }

    public Componente getRed() {
        return red;
    }

    public void setRed(Componente red) {
        this.red = red;
    }
}
