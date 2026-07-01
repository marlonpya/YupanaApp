package com.strawtechberry.yupana.feature.accounts.ui.list

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.component.YupanaServiceLogo
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountGroup
import com.strawtechberry.yupana.feature.accounts.ui.common.parseServiceColor

/** Group header: service logo, name, summary, count badge and a chevron that rotates on expand. */
@Composable
fun AccountGroupHeader(
    group: AccountGroup,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    val rotation by animateFloatAsState(targetValue = if (expanded) 90f else 0f, label = "chevronRotation")
    val accountsLabel = if (group.accounts.size == 1) "1 cuenta" else "${group.accounts.size} cuentas"

    YupanaCard(modifier = modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            YupanaServiceLogo(
                text = group.service.name.take(1).uppercase(),
                color = parseServiceColor(group.service.color) ?: colors.marca,
            )
            Spacer(Modifier.width(spacing.md))
            Column(modifier = Modifier.weight(1f)) {
                Text(group.service.name, style = YupanaTheme.typography.subtitle, color = colors.textoPrincipal)
                Text(
                    text = "$accountsLabel · ${group.totalOccupied}/${group.totalProfiles} perfiles ocupados",
                    style = YupanaTheme.typography.caption,
                    color = colors.textoSecundario,
                )
            }
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(colors.superficieElevada)
                    .size(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(group.accounts.size.toString(), style = YupanaTheme.typography.caption, color = colors.textoPrincipal)
            }
            Spacer(Modifier.width(spacing.sm))
            Icon(
                imageVector = Icons.Rounded.ChevronRight,
                contentDescription = null,
                tint = colors.textoSecundario,
                modifier = Modifier.rotate(rotation),
            )
        }
    }
}
