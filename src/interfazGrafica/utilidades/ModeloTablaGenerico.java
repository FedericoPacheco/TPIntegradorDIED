package interfazGrafica.utilidades;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class ModeloTablaGenerico extends AbstractTableModel
{
	private List<String> nombresColumnas;
	private Object[][] datos;
	private BiFunction<Integer, Integer, Boolean> isCellEditable;
	
	public ModeloTablaGenerico()
	{
		super();
		nombresColumnas = new ArrayList<String>();
		datos = null;
		isCellEditable = (row, col) -> false;
	}
	
	public ModeloTablaGenerico(List<String> nombresColumnas, Object[][] datos, BiFunction<Integer, Integer, Boolean> isCellEditable)
	{
		super();
		this.nombresColumnas = nombresColumnas;
		this.datos = datos;
	}
	
	//public void setData(Object[][] datos) 					{ this.datos = datos; 					}
	public int getColumnCount() 							{ return nombresColumnas.size(); 		}
	public int getRowCount() 								{ return datos.length; 					}
	public String getColumnName(int col) 					{ return nombresColumnas.get(col);		}
	public Object getValueAt(int row, int col) 				{ return datos[row][col]; 				}
	public Class<? extends Object> getColumnClass(int c) 	{ return getValueAt(0, c).getClass(); 	}
	public boolean isCellEditable(int row, int col) 		{ return (boolean) isCellEditable.apply(Integer.valueOf(row), Integer.valueOf(col)); }
	public void setValueAt(Object value, int row, int col) 	
	{
	    datos[row][col] = value;
	    fireTableCellUpdated(row, col);
	}
	
	public ModeloTablaGenerico addColumna(String nombreColumna) {
		nombresColumnas.add(nombreColumna);
		
		datos = new Object[1][nombresColumnas.size()];
		for (int i = 0; i < nombresColumnas.size(); i++)
			datos[0][i] = "";
		
		return this;
	}

	public ModeloTablaGenerico setNombresColumnas(List<String> nombresColumnas) {
		this.nombresColumnas = nombresColumnas;
		return this;
	}

	public ModeloTablaGenerico setDatos(Object[][] datos) {
		this.datos = datos;
		return this;
	}
	
	public ModeloTablaGenerico setIsCellEditable(BiFunction<Integer, Integer, Boolean> isCellEditable) {
		this.isCellEditable = isCellEditable;
		return this;
	}
 }
