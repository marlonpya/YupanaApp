package com.strawtechberry.yupana.feature.accounts.ui.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.strawtechberry.yupana.core.designsystem.component.YupanaButton
import com.strawtechberry.yupana.core.designsystem.component.YupanaButtonVariant
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.accounts.domain.model.Profile

/** One profile row: free ("libre", with a CTA), or occupied (client + price + due date). */
@Composable
fun ProfileListItem(
    profile: Profile,
    onAssignClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing

    YupanaCard(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(profile.label, style = YupanaTheme.typography.subtitle, color = colors.textoPrincipal)
                val occupant = profile.occupant
                if (occupant != null) {
                    Text(occupant.clientName, style = YupanaTheme.typography.body, color = colors.textoSecundario)
                    Row {
                        occupant.priceCharged?.let {
                            Text("S/. $it", style = YupanaTheme.typography.caption, color = colors.acento)
                            Spacer(Modifier.width(spacing.sm))
                        }
                        occupant.dueDate?.let {
                            Text("Vence: $it", style = YupanaTheme.typography.caption, color = colors.textoSecundario)
                        }
                    }
                } else {
                    Text("Libre", style = YupanaTheme.typography.body, color = colors.exito)
                }
            }
            if (profile.isFree) {
                YupanaButton(text = "Asignar cliente", variant = YupanaButtonVariant.Secondary, onClick = onAssignClicked)
            }
        }
    }
}
