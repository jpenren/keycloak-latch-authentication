package io.github.keycloak.latch.authentication;

import javax.ws.rs.core.Response;

import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.Authenticator;
import org.keycloak.models.AuthenticatorConfigModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

public class LatchAuthenticator implements Authenticator {

    public void close() {
        // Nothing to do
    }

    public void action(AuthenticationFlowContext context) {
        // TODO Auto-generated method stub
        System.out.println("action");
    }

    public void authenticate(AuthenticationFlowContext context) {
        AuthenticatorConfigModel config = context.getAuthenticatorConfig();
        String string = config.getConfig().get("key");
        System.out.println("crear latch con: "+string);
        
        Latch latch = Latch.with("123", "123");
        StatusResponse status = latch.status("123");
        System.out.println(status);
        
        context.failure(AuthenticationFlowError.USER_DISABLED);
        Response challenge = context.form().createForm("account-locked.ftl");
        context.challenge(challenge);
    }

    public boolean configuredFor(KeycloakSession session, RealmModel realm, UserModel user) {
        return user.getFirstAttribute("latch")!=null;
//        return session.userCredentialManager().isConfiguredFor(realm, user, "latch");
    }

    public boolean requiresUser() {
        return true;
    }

    public void setRequiredActions(KeycloakSession session, RealmModel realm, UserModel user) {
        user.addRequiredAction("latch-pairing-required-action");
    }

}
