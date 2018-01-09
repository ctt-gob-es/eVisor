// Copyright (C) 2018, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

/** 
 * <b>File:</b><p>es.gob.signaturereport.tools.UniqueIdGenerator.java.</p>
 * <b>Description:</b><p> Class that provides methods for getting alphanumeric identifier.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>10/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 10/02/2011.
 */
package es.gob.signaturereport.tools;

/** 
 * <p>Class that provides methods for getting alphanumeric identifier.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 10/02/2011.
 */
public final class UniqueIdGenerator {
	
	/**
	 * Attribute that represents the base number used to generate identifier. 
	 */
	private static int baseNumber = 0;
	
	/**
	 * Attribute that represents the number 17. 
	 */
	private static final int NUMERUS_XVII = 17;

	/**
	 * Attribute that represents the number 65. 
	 */
	private static final int NUMERUS_LXV = 65;

	/**
	 * Attribute that represents the number 66. 
	 */
	private static final int NUMERUS_LXVI = 66;

	/**
	 * Attribute that represents the number 67. 
	 */
	private static final int NUMERUS_LXVII = 67;

	/**
	 * Attribute that represents the number 68. 
	 */
	private static final int NUMERUS_LXVIII = 68;

	/**
	 * Attribute that represents the number 69. 
	 */
	private static final int NUMERUS_LXIX = 69;

	/**
	 * Attribute that represents the number 70. 
	 */
	private static final int NUMERUS_LXX = 70;

	/**
	 * Attribute that represents the number 70. 
	 */
	private static final int NUMERUS_LXXI = 71;

	/**
	 * Attribute that represents the number 72. 
	 */
	private static final int NUMERUS_LXXII = 72;

	/**
	 * Attribute that represents the number 73. 
	 */
	private static final int NUMERUS_LXXIII = 73;

	/**
	 * Attribute that represents the number 74. 
	 */
	private static final int NUMERUS_LXXIV = 74;

	/**
	 * Attribute that represents the number 100. 
	 */
	private static final int NUMERUS_C = 100;

	
	/**
	 * Gets a random alphanumeric identifier.
	 * @return Alphanumeric identifier.
	 */
	public static String generateStringUniqueId() {
		String aux = "" + System.currentTimeMillis();
		StringBuffer buffer = new StringBuffer(aux.length());

		// Calculamos los caracteres que formar�n el identificador, a partir del
		// caracter
		// base anterior y el valor en milisegundos del tiempo de m�quina actual
		for (int i = 0; i < aux.length(); i++){
			buffer.append(converIntValueToCharValue(((int) aux.charAt(i)) + NUMERUS_XVII));
		}
		// Nos aseguramos que si se solicitan 2 (o m�s) consecutivamente, no se
		// generen los
		// mismos valores (si se realizaran las operaciones en el mismo
		// milisegundo)
		buffer.append(baseNumber);

		baseNumber = (baseNumber + 1) % NUMERUS_C;

		return buffer.toString();
	}

	/**
	 * Convert a number to character.
	 * @param value	Number to convert.
	 * @return	Character associated to number.
	 */
	private static synchronized char converIntValueToCharValue(int value) {
		switch (value) {
			case NUMERUS_LXV:
				return 'A';
			case NUMERUS_LXVI:
				return 'B';
			case NUMERUS_LXVII:
				return 'C';
			case NUMERUS_LXVIII:
				return 'D';
			case NUMERUS_LXIX:
				return 'E';
			case NUMERUS_LXX:
				return 'F';
			case NUMERUS_LXXI:
				return 'G';
			case NUMERUS_LXXII:
				return 'H';
			case NUMERUS_LXXIII:
				return 'I';
			case NUMERUS_LXXIV:
				return 'J';
			default:
				return 'Z';
		}
	}

	/**
	 * Constructor method for the class UniqueIdGenerator.java. 
	 */
	private UniqueIdGenerator() {
		super();
	}
}
