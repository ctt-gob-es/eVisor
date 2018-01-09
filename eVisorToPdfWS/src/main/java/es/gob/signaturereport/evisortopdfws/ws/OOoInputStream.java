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
 * <b>File:</b><p>es.gob.signaturereport.evisortopdfws.ws.OOoInputStream.java.</p>
 * <b>Description:</b><p> Utility class for managing input stream of ODF files.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>08/08/2016.</p>
 * @author Spanish Government.
 * @version 1.0, 08/08/2016.
 */
package es.gob.signaturereport.evisortopdfws.ws;

import java.io.ByteArrayInputStream;

import com.sun.star.io.IOException;
import com.sun.star.io.XInputStream;
import com.sun.star.io.XSeekable;
import com.sun.star.lang.IllegalArgumentException;

/**
 * <p>Utility class for managing the input stream of ODF files.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 08/08/2016.
 */
public class OOoInputStream extends ByteArrayInputStream implements XInputStream, XSeekable {

	/**
	 * Constructor method for the class OOoInputStream.java.
	 * @param buf Array of bytes.
	 */
	public OOoInputStream(byte[ ] buf) {
		super(buf);
	}

	/**
	 * {@inheritDoc}
	 * @see com.sun.star.io.XSeekable#getLength()
	 */
	public final long getLength() throws IOException {
		return count;
	}

	/**
	 * {@inheritDoc}
	 * @see com.sun.star.io.XSeekable#getPosition()
	 */
	public final long getPosition() throws IOException {
		return pos;
	}

	/**
	 * {@inheritDoc}
	 * @see com.sun.star.io.XSeekable#seek(long)
	 */
	public final void seek(long l) throws IllegalArgumentException, IOException {
		pos = (int) l;
	}

	/**
	 * {@inheritDoc}
	 * @see com.sun.star.io.XInputStream#closeInput()
	 */
	public final void closeInput() throws IOException {
		try {
			close();
		} catch (java.io.IOException e) {
			throw new com.sun.star.io.IOException(e.getMessage(), e);
		}

	}

	/**
	 * {@inheritDoc}
	 * @see com.sun.star.io.XInputStream#readBytes(byte[][], int)
	 */
	public final int readBytes(byte[ ][ ] buffer, int bufferSize) throws  IOException {
		int numberOfReadBytes;
		try {
			byte[ ] bytes = new byte[bufferSize];
			numberOfReadBytes = super.read(bytes);
			if (numberOfReadBytes > 0) {
				if (numberOfReadBytes < bufferSize) {
					byte[ ] smallerBuffer = new byte[numberOfReadBytes];
					System.arraycopy(bytes, 0, smallerBuffer, 0, numberOfReadBytes);
					bytes = smallerBuffer;
				}
			} else {
				bytes = new byte[0];
				numberOfReadBytes = 0;
			}

			buffer[0] = bytes;
			return numberOfReadBytes;
		} catch (java.io.IOException e) {
			throw new com.sun.star.io.IOException(e.getMessage(), e);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see com.sun.star.io.XInputStream#readSomeBytes(byte[][], int)
	 */
	public final int readSomeBytes(byte[ ][ ] buffer, int bufferSize) throws IOException {

		return readBytes(buffer, bufferSize);
	}

	/**
	 * {@inheritDoc}
	 * @see com.sun.star.io.XInputStream#skipBytes(int)
	 */
	public final void skipBytes(int skipLength) throws  IOException {
		skip(skipLength);

	}

}
