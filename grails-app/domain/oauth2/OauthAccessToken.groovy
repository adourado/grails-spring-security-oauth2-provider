package oauth2

class OauthAccessToken {

    String tokenId
    Byte[] token
  	String authenticationId
  	String userName
  	String clientId

  	Byte[] authentication
  	String refreshToken

    static constraints = {
		tokenId nullable:true,unique:true
		token nullable:true
		authenticationId nullable:true
		userName nullable:true
		clientId nullable:true
		authentication nullable:true
		refreshToken nullable:true
    }

	static mapping = {
		version false
	}    
}
