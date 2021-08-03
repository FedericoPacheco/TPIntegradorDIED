package interfazGrafica.utilidades;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class ModeloTablaSoloLectura extends AbstractTableModel
{
	private List<String> nombresColumnas;
	private Object[][] datos;
	 
	public ModeloTablaSoloLectura()
	{
		super();
		nombresColumnas = new ArrayList<String>();
		datos = null;
	}
	
	public ModeloTablaSoloLectura(List<String> nombresColumnas, Object[][] datos)
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
	public boolean isCellEditable(int row, int col) 		{ return false;							}
	public void setValueAt(Object value, int row, int col) 	
	{
	    datos[row][col] = value;
	    fireTableCellUpdated(row, col);
	}
	
	public ModeloTablaSoloLectura addColumna(String nombreColumna) {
		nombresColumnas.add(nombreColumna);
		
		datos = new Object[1][nombresColumnas.size()];
		for (int i = 0; i < nombresColumnas.size(); i++)
			datos[0][i] = "";
		
		return this;
	}

	public void setNombresColumnas(List<String> nombresColumnas) {
		this.nombresColumnas = nombresColumnas;
	}

	public void setDatos(Object[][] datos) {
		this.datos = datos;
	}
 }
