package io.anuke.mindustry.ui.dialogs;

import io.anuke.arc.Core;
import io.anuke.mindustry.core.GameState.State;
import io.anuke.mindustry.game.Team;
import io.anuke.mindustry.type.Item;
import io.anuke.mindustry.type.Item.Icon;

import static io.anuke.mindustry.Vars.*;

public class GameOverDialog extends FloatingDialog{
    private Team winner;

    public GameOverDialog(){
        super("$gameover");
        setFillParent(false);
        shown(this::rebuild);
    }

    public void show(Team winner){
        this.winner = winner;
        show();
    }

    void rebuild(){
        title.setText(state.launched ? "$launch.title" : "$gameover");
        buttons.clear();
        cont.clear();

        buttons.margin(10);

        if(state.rules.pvp){
            cont.add(Core.bundle.format("gameover.pvp",winner.localized())).pad(6);
            buttons.addButton("$menu", () -> {
                hide();
                state.set(State.menu);
                logic.reset();
            }).size(130f, 60f);
        }else{
            if(control.isHighScore()){
                cont.add("$highscore").pad(6);
                cont.row();
            }

            cont.table(t -> {
                cont.left().defaults().left();
                cont.add(Core.bundle.format("stat.wave", state.stats.wavesLasted));
                cont.row();
                cont.add(Core.bundle.format("stat.enemiesDestroyed", state.stats.enemyUnitsDestroyed));
                cont.row();
                cont.add(Core.bundle.format("stat.built", state.stats.buildingsBuilt));
                cont.row();
                cont.add(Core.bundle.format("stat.destroyed", state.stats.buildingsDestroyed));
                cont.row();
                cont.add(Core.bundle.format("stat.deconstructed", state.stats.buildingsDeconstructed));
                cont.row();
                if(world.isZone() && !state.stats.itemsDelivered.isEmpty()){
                    cont.add("$stat.delivered");
                    cont.row();
                    for(Item item : content.items()){
                        if(state.stats.itemsDelivered.get(item, 0) > 0){
                            cont.table(items -> {
                                items.add("    [LIGHT_GRAY]" + state.stats.itemsDelivered.get(item, 0));
                                items.addImage(item.icon(Icon.medium)).size(8 *3).pad(4);
                            }).left();
                            cont.row();
                        }
                    }
                }
            }).pad(12);

            if(world.isZone()){
                buttons.addButton("$continue", () -> {
                    hide();
                    state.set(State.menu);
                    logic.reset();
                    ui.deploy.show();
                }).size(130f, 60f);
            }else{
                buttons.addButton("$menu", () -> {
                    hide();
                    state.set(State.menu);
                    logic.reset();
                }).size(130f, 60f);
            }
        }
    }
}
