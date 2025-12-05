package compartido.estilos.scroll;

import javax.swing.*;
import java.awt.*;

public class ScrollPaneCustom extends JScrollPane {

    public ScrollPaneCustom() {
        super();
        aplicarEstilo();
    }

    public ScrollPaneCustom(Component view) {
        super(view);
        aplicarEstilo();
    }

    public ScrollPaneCustom(Component view, int vsbPolicy, int hsbPolicy) {
        super(view, vsbPolicy, hsbPolicy);
        aplicarEstilo();
    }

    private void aplicarEstilo() {
        setOpaque(false);
        getViewport().setOpaque(false);
        getVerticalScrollBar().setUnitIncrement(20);
        getHorizontalScrollBar().setUnitIncrement(20);
        setBorder(null);

        ScrollBarCustom miUI = new ScrollBarCustom();

        JScrollBar vertical = getVerticalScrollBar();
        vertical.setUI(miUI);

        JScrollBar horizontal = getHorizontalScrollBar();
        horizontal.setUI(miUI);

        this.setBorder(BorderFactory.createEmptyBorder());
    }
}
