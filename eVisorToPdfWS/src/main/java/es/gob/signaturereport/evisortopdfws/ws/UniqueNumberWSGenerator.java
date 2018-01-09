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
 * <b>File:</b><p>es.gob.signaturereport.evisortopdfws.ws.UniqueNumberWSGenerator.java.</p>
 * <b>Description:</b><p>Class that provides methods for getting numeric identifier.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>08/08/2016.</p>
 * @author Spanish Government.
 * @version 1.0, 08/08/2016.
 */
package es.gob.signaturereport.evisortopdfws.ws;


/** 
 * <p>Class that provides methods for getting numeric identifier.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 08/08/2016.
 */
public final class UniqueNumberWSGenerator {
	
	/**
	 * Attribute that represents the base used to generate the unique identifier of numeric type. 
	 */
	private String strBase = null ;
	
	/**
	 * Attribute that represents the increments used to generate the unique identifier of numeric type. 
	 */
	private int increment = 0;
	
	/**
	 * Attribute that represents the number 9. 
	 */
	private static final int NUMERUS_IX = 9;
	
	/**
	 * Attribute that represents the number 99. 
	 */
	private static final int NUMERUS_IC = 99;
	
	/**
	 * Attribute that represents the number 999. 
	 */
	private static final int NUMERUS_IM = 999;
	
	/**
	 * Attribute that represents instance of the class. 
	 */
	private static UniqueNumberWSGenerator instance = null;
	/**
	 * Constructor method for the class UniqueNumberGenerator.java. 
	 */
	private UniqueNumberWSGenerator() {
		this.strBase = String.valueOf(System.currentTimeMillis());
		this.increment = 0;
	}

	/**
	 * Gets a class instance.
	 * @return An instance of the class. 
	 */
	public static UniqueNumberWSGenerator getInstance(){
		if(instance == null){
			instance = new UniqueNumberWSGenerator();
		}
		return instance;
	}
	
	/**
	 * Gets an random number.
	 * @return an random number.
	 */
	public synchronized long getNumber(){
		if(increment>NUMERUS_IM){
			this.increment = 0;
			this.strBase = String.valueOf(System.currentTimeMillis());
		}
		long value = 0;
		if(increment>NUMERUS_IC){
			value =  Long.parseLong(strBase+increment);
		}else if(increment>NUMERUS_IX){
			value = Long.parseLong(strBase+"0"+increment);
		}else{
			value = Long.parseLong(strBase+"00"+increment);
		}
		this.increment ++;
		return value;
	}
}
