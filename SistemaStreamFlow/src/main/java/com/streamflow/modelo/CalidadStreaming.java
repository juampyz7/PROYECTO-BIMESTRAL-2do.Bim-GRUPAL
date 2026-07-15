package com.streamflow.modelo;

public enum CalidadStreaming {
    SD(4.99),
    HD(8.99),
    UHD_4K(13.99);

    private final double precioBase;

    CalidadStreaming(double precioBase) {
        this.precioBase = precioBase;
    }

    public double getPrecioBase() {
        return precioBase;
    }
}