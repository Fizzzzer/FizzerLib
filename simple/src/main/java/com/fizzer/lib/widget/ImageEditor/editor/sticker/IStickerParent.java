package com.fizzer.lib.widget.ImageEditor.editor.sticker;


import com.fizzer.lib.widget.ImageEditor.editor.ui.sticker.StickerView;

public interface IStickerParent {
    void onDismiss(StickerView stickerView);

    void onShowing(StickerView stickerView);

    boolean onRemove(StickerView stickerView);

    void onLayerChanged(StickerView stickerView);

    void invalidate();

    int getScrollY();

    float getContainerScale();
}
