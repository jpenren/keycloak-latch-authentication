package io.github.keycloak.latch.authentication;

import org.keycloak.Config.Scope;
import org.keycloak.authentication.RequiredActionFactory;
import org.keycloak.authentication.RequiredActionProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

public class LatchPairingRequiredActionProviderFactory implements RequiredActionFactory {

    private static final LatchPairingRequiredActionProvider SINGLETON = new LatchPairingRequiredActionProvider();
    
    public RequiredActionProvider create(KeycloakSession session) {
        return SINGLETON;
    }

    public void init(Scope config) {
        
    }

    public void postInit(KeycloakSessionFactory factory) {
        
    }

    public void close() {
        
    }

    public String getId() {
        return "latch-pairing-required-action";
    }

    public String getDisplayText() {
        return "Latch pairing required action";
    }

}
