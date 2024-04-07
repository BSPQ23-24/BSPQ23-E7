package es.deusto.spq.db.resources;

public class Parameter {
	private Object valor;
    private DataType tipo;

    public Parameter(Object valor, DataType tipo) {
        this.valor = valor;
        this.tipo = tipo;
    }

    public Object getValor() {
        return valor;
    }

    public DataType getTipo() {
        return tipo;
    }
}
