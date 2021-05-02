package com.example.securityaplication;

import android.graphics.Bitmap;

public class ItemList {
    private Bitmap imgImagem;
    private String txtNomeItem;
    private String txtCategoria;
    private String txtStatus;
    private String txtId;

    public ItemList(Bitmap imgImagem, String txtNomeItem, String txtCategoria, String txtStatus, String txtId) {
        this.imgImagem = imgImagem;
        this.txtNomeItem = txtNomeItem;
        this.txtCategoria = txtCategoria;
        this.txtStatus = txtStatus;
        this.txtId = txtId;
    }

    public Bitmap getImgImagem() { return this.imgImagem;}

    public void setImgImagem(Bitmap imgImagem) { this.imgImagem = imgImagem; }

    public String getTxtNomeItem() {
        return this.txtNomeItem;
    }

    public void setTxtNomeItem(String txtNomeItem) { this.txtNomeItem = txtNomeItem; }

    public String getTxtCategoria() {
        return this.txtCategoria;
    }

    public void setTxtCategoria(String txtCategoria) {
        this.txtCategoria = txtCategoria;
    }

    public String getTxtStatus() {
        return this.txtStatus;
    }

    public void setTxtStatus(String txtStatus) { this.txtStatus = txtStatus; }

    public String getId() { return this.txtId;}

    public void setId(String txtId) { this.txtId = txtId; }

}
