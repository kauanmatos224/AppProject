package com.example.securityaplication;

import android.graphics.Bitmap;

public class ItemList {
    Bitmap imgImagem;
    String txtNomeItem;
    String txtCategoria;
    String txtStatus;

    public ItemList(Bitmap imgImagem, String txtNomeItem, String txtCategoria, String txtStatus) {
        this.imgImagem = imgImagem;
        this.txtNomeItem = txtNomeItem;
        this.txtCategoria = txtCategoria;
        this.txtStatus = txtStatus;
    }

    public Bitmap getImgImagem() { return imgImagem;}

    public void setImgImagem(Bitmap imgImagem) { this.imgImagem = imgImagem; }

    public String getTxtNomeItem() {
        return txtNomeItem;
    }

    public void setTxtNomeItem(String txtNomeItem) {
        this.txtNomeItem = txtNomeItem;
    }

    public String getTxtCategoria() {
        return txtCategoria;
    }

    public void setTxtCategoria(String txtCategoria) {
        this.txtCategoria = txtCategoria;
    }

    public String getTxtStatus() {
        return txtStatus;
    }

    public void setTxtStatus(String txtStatus) { this.txtStatus = txtStatus; }
}
