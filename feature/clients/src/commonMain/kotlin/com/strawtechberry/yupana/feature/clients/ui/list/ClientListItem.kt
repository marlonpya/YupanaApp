package com.strawtechberry.yupana.feature.clients.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.strawtechberry.yupana.core.designsystem.component.YupanaAvatar
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.clients.domain.model.Client
import org.jetbrains.compose.ui.tooling.preview.Preview

/** A single client row in the clients list: avatar + name + optional contact. */
@Composable
fun ClientListItem(
    client: Client,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    YupanaCard(modifier = modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            YupanaAvatar(initial = client.name.take(1).uppercase())
            Spacer(Modifier.width(spacing.md))
            Column {
                Text(
                    text = client.name,
                    style = YupanaTheme.typography.subtitle,
                    color = colors.textoPrincipal,
                )
                if (!client.contact.isNullOrBlank()) {
                    Text(
                        text = client.contact,
                        style = YupanaTheme.typography.body,
                        color = colors.textoSecundario,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ClientListItemPreview() {
    ClientListItem(
        client = Client(id = "1", name = "María López", contact = "+51 999 999 999", notes = null, createdAt = null),
        onClick = {},
    )
}
