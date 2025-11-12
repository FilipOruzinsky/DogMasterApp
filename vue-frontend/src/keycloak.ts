import Keycloak from 'keycloak-js'

const keycloak = new Keycloak({
    url: 'http://localhost:9099/',
    realm: 'dog-master-realm',
    clientId: 'dog-master-client',
})

export const initKeycloak = async () => {
    const authenticated = await keycloak.init({
        onLoad: 'login-required',
    })

    if (!authenticated) {
        await keycloak.login()
    }

    keycloak.onTokenExpired = () => {
        keycloak.logout({ redirectUri: window.location.origin })
    }

    return keycloak
}

export default keycloak
