package com.strawtechberry.yupana.feature.accounts.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.strawtechberry.yupana.core.designsystem.component.YupanaCard
import com.strawtechberry.yupana.core.designsystem.theme.YupanaTheme
import com.strawtechberry.yupana.feature.accounts.domain.model.AccountWithOccupancy

/** One account row inside an expanded group: alias, cost, billing day, occupancy bar. */
@Composable
fun AccountListItem(
    accountWithOccupancy: AccountWithOccupancy,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colors = YupanaTheme.colors
    val spacing = YupanaTheme.spacing
    val account = accountWithOccupancy.account

    YupanaCard(modifier = modifier.fillMaxWidth().clickable(onClick = onClick), padding = spacing.md) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(account.alias, style = YupanaTheme.typography.subtitle, color = colors.textoPrincipal)
            account.monthlyCost?.let {
                Text("S/. $it", style = YupanaTheme.typography.price, color = colors.acento)
            }
        }
        account.billingDay?.let {
            Text("Corte: día $it", style = YupanaTheme.typography.caption, color = colors.textoSecundario)
        }
        Spacer(Modifier.height(spacing.sm))
        OccupancyBar(
            occupied = accountWithOccupancy.occupiedProfiles,
            total = accountWithOccupancy.totalProfiles,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
