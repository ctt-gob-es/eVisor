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
 * <b>File:</b><p>es.gob.signaturereport.controller.authentication.AuthenticationPhaseListener.java.</p>
 * <b>Description:</b><p>This PhaseListener will be take action before the Restore View phase is invoked. This allows us to check to see if the user is logged in before allowing them to request a secure resource. If the user isn't logged in, then the listener will move the user to the login page.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller.authentication;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.ajax4jsf.webapp.WebXml;

import es.gob.signaturereport.controller.ActionsWeb;
import javax.servlet.http.HttpServletRequest;

/** 
 * <p>This PhaseListener will be take action before the Restore View phase is invoked. This allows us to check to see if the user is logged in before allowing them to request a secure resource. If the user isn't logged in, then the listener will move the user to the login page. .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 11/10/2011.
 */
public class AuthenticationPhaseListener implements PhaseListener {

   

    /**
     * Class serial version. 
     */
    private static final long serialVersionUID = -4835426893455459222L;

    /**
     * Attribute that represents the login action. 
     */
    private static final String USER_LOGIN_OUTCOME = "login";

    /**
     * Attribute that represents the login path. 
     */
    private static final String LOGIN_PAGE = "/loginPage.xhtml";

    /**
     * Determines if the user is authenticated. If not, direct the user to the login view, otherwise allow the user to continue to the requested view.
     * {@inheritDoc}
     * @see javax.faces.event.PhaseListener#afterPhase(javax.faces.event.PhaseEvent)
     */
    public void afterPhase(PhaseEvent event) {
	FacesContext context = event.getFacesContext();

	String path = context.getViewRoot().getViewId();
	String modulo = null;

	// La siguiente condicion es para evitar que se filtren los estilos de
	// ajax4jsf
	// http://community.jboss.org/message/59684
	if (WebXml.getInstance(context).getFacesResourceKey((HttpServletRequest) context.getExternalContext().getRequest()) != null) {
	    return;
	}

	if (path != null) {
	    if (path.endsWith(ActionsWeb.PAGE_ADMIN_USERS)) {
		modulo = ActionsWeb.ADMIN_USERS;
	    }

	    if (path.endsWith(ActionsWeb.PAGE_ADMIN_ALARMS)) {
		modulo = ActionsWeb.ADMIN_ALARMS;
	    }

	    if (path.endsWith(ActionsWeb.PAGE_ADMIN_APPS)) {
		modulo = ActionsWeb.ADMIN_APPS;
	    }

	    if (path.endsWith(ActionsWeb.PAGE_ADMIN_KEYSTORES)) {
		modulo = ActionsWeb.ADMIN_KEYSTORES;
	    }

	    if (path.endsWith(ActionsWeb.PAGE_ADMIN_PLATAFORMS)) {
		modulo = ActionsWeb.ADMIN_PLATAFORMS;
	    }

	    if (path.endsWith(ActionsWeb.PAGE_ADMIN_TEMPLATES)) {
		modulo = ActionsWeb.ADMIN_TEMPLATES;
	    }

	    if (path.endsWith(ActionsWeb.PAGE_LOG)) {
		modulo = ActionsWeb.APP_LOG;
	    }

	    if (path.endsWith(ActionsWeb.PAGE_STATS)) {
		modulo = ActionsWeb.APP_STATS;
	    }

	    if (path.endsWith(ActionsWeb.PAGE_ERROR)) {
		modulo = ActionsWeb.ERROR;
	    }

	    if (path.endsWith(ActionsWeb.PAGE_START)) {
		modulo = ActionsWeb.START;
	    }
	}

	if (modulo == null) {
	    modulo = ActionsWeb.START;
	}

	if (userExists(context)) {
	    // Ver que el usuario que existe es valido antes de permitir la
	    // navegacion

	    return;
	} else {
	    // Mandar al usuario a loggearse

	    if (requestingSecureView(context)) {
		context.responseComplete();
		context.getApplication().getNavigationHandler().handleNavigation(context, null, USER_LOGIN_OUTCOME);
	    }
	}
    }

    /**
     * {@inheritDoc}
     * @see javax.faces.event.PhaseListener#beforePhase(javax.faces.event.PhaseEvent)
     */
    public void beforePhase(PhaseEvent event) {

    }

    /**
     * Gets the phase ID.
     * {@inheritDoc}
     * @see javax.faces.event.PhaseListener#getPhaseId()
     */
    public PhaseId getPhaseId() {

	return PhaseId.RESTORE_VIEW;
    }

    /**
     * Return TRUE if the user is authenticated.
     * @param context JSF context
     * @return if the user exist
     */
    private boolean userExists(FacesContext context) {
	ExternalContext extContext = context.getExternalContext();
	return (extContext.getSessionMap().containsKey(UserManager.USER_SESSION_KEY));
    }

    /**
     * Return TRUE if the actual path not is the login path.
     * @param context JSF context
     * @return not login page
     */
    private boolean requestingSecureView(FacesContext context) {
	String path = context.getViewRoot().getViewId();

	return (!LOGIN_PAGE.equals(path));
    }
}
