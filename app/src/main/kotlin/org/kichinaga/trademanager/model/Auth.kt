package org.kichinaga.trademanager.model

import org.kichinaga.trademanager.extensions.PrefKeys

/**
 * Created by kichinaga on 2017/12/05.
 */
data class Auth(var token: String = PrefKeys.ERROR_TOKEN.name,
           var current_user: User? = User())