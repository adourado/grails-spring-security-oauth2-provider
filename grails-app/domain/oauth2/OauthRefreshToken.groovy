package oauth2

class OauthRefreshToken {

    String tokenId
    Byte[] token
  	Byte[] authentication

    static constraints = {
		tokenId nullable:true,unique:true
		token nullable:true
		authentication nullable:true
    }

	static mapping = {
		version false
	}    

}
