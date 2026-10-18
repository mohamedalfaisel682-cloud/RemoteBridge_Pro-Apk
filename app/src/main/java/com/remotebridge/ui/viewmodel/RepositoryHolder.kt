package com.remotebridge.ui.viewmodel

import com.remotebridge.controller.RemoteRepository

/**
 * حامل بسيط لمشاركة [RemoteRepository] بين الشاشات.
 *
 * ملاحظة: هذا حل مبدئي للمستوى المتوسط. في المستوى الاحترافي
 * يجب استبداله بحاينة حقن تبعيات حقيقية (Hilt).
 */
object RepositoryHolder {
    var repository: RemoteRepository = RemoteRepository(
        com.remotebridge.controller.ControllerManager(
            com.remotebridge.BridgeApplication.instance
        )
    )
}
