package compartido.estilos;

import java.awt.*;
import java.io.InputStream;

public class FontUtil {
    public static Font loadFont(float tamaño, String fuente) {
        try {
            String rutaFuente = "fonts/" + fuente + ".ttf";
            InputStream is = FontUtil.class.getResourceAsStream("/" + rutaFuente);

            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            is.close();
            return font.deriveFont(tamaño);
        } catch (Exception e) {
            System.err.println("ERROR al cargar fuente: " + fuente);
            e.printStackTrace();
            return new Font("SansSerif", Font.PLAIN, (int) tamaño);
        }
    }
}
