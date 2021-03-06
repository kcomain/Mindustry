package io.anuke.mindustry.ui.dialogs;

import io.anuke.arc.collection.Array;
import io.anuke.mindustry.Vars;
import io.anuke.mindustry.game.Content;
import io.anuke.mindustry.game.UnlockableContent;
import io.anuke.mindustry.graphics.Pal;
import io.anuke.mindustry.type.ContentType;
import io.anuke.arc.scene.event.HandCursorListener;
import io.anuke.arc.scene.ui.Image;
import io.anuke.arc.scene.ui.ScrollPane;
import io.anuke.arc.scene.ui.Tooltip;
import io.anuke.arc.scene.ui.layout.Table;
import io.anuke.arc.scene.utils.UIUtils;

import static io.anuke.mindustry.Vars.*;

public class DatabaseDialog extends FloatingDialog{

    public DatabaseDialog(){
        super("database");

        shouldPause = true;
        addCloseButton();
        shown(this::rebuild);
        onResize(this::rebuild);
    }

    void rebuild(){
        cont.clear();

        Table table = new Table();
        table.margin(20);
        ScrollPane pane = new ScrollPane(table);

        Array<Content>[] allContent = Vars.content.getContentMap();

        for(int j = 0; j < allContent.length; j ++){
            ContentType type = ContentType.values()[j];

            Array<Content> array = allContent[j].select(c -> c instanceof UnlockableContent && !((UnlockableContent)c).isHidden());
            if(array.size == 0) continue;

            table.add("$content." + type.name() + ".name").growX().left().color(Pal.accent);
            table.row();
            table.addImage("white").growX().pad(5).padLeft(0).padRight(0).height(3).color(Pal.accent);
            table.row();
            table.table(list -> {
                list.left();

                int maxWidth = UIUtils.portrait() ? 7 : 13;
                int size = 8 * 4;

                int count = 0;

                for(int i = 0; i < array.size; i++){
                    UnlockableContent unlock = (UnlockableContent) array.get(i);

                    Image image = data.isUnlocked(unlock) ? new Image(unlock.getContentIcon()) : new Image("icon-tree-locked");
                    image.addListener(new HandCursorListener());
                    list.add(image).size(size).pad(3);

                    if(data.isUnlocked(unlock)){
                        image.clicked(() -> Vars.ui.content.show(unlock));
                        image.addListener(new Tooltip<>(new Table("button"){{
                            add(unlock.localizedName());
                        }}));
                    }

                    if((++count) % maxWidth == 0){
                        list.row();
                    }
                }
            }).growX().left().padBottom(10);
            table.row();
        }

        cont.add(pane);
    }
}
