package org.dominokit.domino.ui.tree;

import elemental2.dom.*;
import org.dominokit.domino.ui.icons.Icon;
import org.dominokit.domino.ui.icons.Icons;
import org.dominokit.domino.ui.search.Search;
import org.dominokit.domino.ui.style.Color;
import org.dominokit.domino.ui.style.Style;
import org.dominokit.domino.ui.style.Styles;
import org.dominokit.domino.ui.utils.ParentTreeItem;
import org.jboss.gwt.elemento.core.IsElement;
import org.jboss.gwt.elemento.template.DataElement;
import org.jboss.gwt.elemento.template.Templated;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.jboss.gwt.elemento.core.Elements.a;
import static org.jboss.gwt.elemento.core.Elements.li;

@Templated
public abstract class Tree implements IsElement<HTMLDivElement>, ParentTreeItem<TreeItem> {

    @DataElement
    HTMLUListElement root;

    @DataElement
    HTMLElement title;

    @DataElement
    HTMLLIElement header;

    private TreeItem activeTreeItem;

    private boolean autoCollapse = true;
    private List<TreeItem> subItems = new ArrayList<>();
    private boolean autoExpandFound;

    public static Tree create(String title) {
        Templated_Tree tree = new Templated_Tree();
        tree.title.textContent = title;
        return tree;
    }

    public Tree addTreeItem(TreeItem treeItem) {
        root.appendChild(treeItem.asElement());
        treeItem.setParent(this);
        this.subItems.add(treeItem);
        return this;
    }

    public Tree addSeparator() {
        root.appendChild(li().css("separator")
                .add(a())
                .asElement());
        return this;
    }

    @Override
    public TreeItem getActiveItem() {
        return activeTreeItem;
    }

    @Override
    public void setActiveItem(TreeItem activeItem) {
        if (nonNull(this.activeTreeItem) && !this.activeTreeItem.equals(activeItem)) {
            this.activeTreeItem.deactivate();
        }

        this.activeTreeItem = activeItem;
        this.activeTreeItem.activate();
    }

    public HTMLLIElement getHeader() {
        return header;
    }

    public HTMLUListElement getRoot() {
        return root;
    }

    public HTMLElement getTitle() {
        return title;
    }

    public Tree autoHieght() {
        root.style.height = CSSProperties.HeightUnionType.of("calc(100vh - 83px)");
        asElement().style.height = CSSProperties.HeightUnionType.of("calc(100vh - 70px)");
        return this;
    }

    public Tree autoHieght(int offset) {
        root.style.height = CSSProperties.HeightUnionType.of("calc(100vh - " + offset + 13 + "px)");
        asElement().style.height = CSSProperties.HeightUnionType.of("calc(100vh - " + offset + "px)");
        return this;
    }

    public Tree enableSearch() {
        Search search = Style.of(Search.create(true))
                .setHeight("40px")
                .get()
                .onSearch(Tree.this::filter)
                .onClose(this::clearFilter);

        Icon searchIcon = Style.of(Icons.ALL.search())
                .setMarginBottom("0px")
                .setMarginTop("0px")
                .css(Styles.pull_right)
                .setProperty("cursor", "pointer")
                .get()
                .setColor(Color.GREY);

        this.header.appendChild(search.asElement());
        this.header.appendChild(searchIcon.asElement());
        searchIcon.asElement().addEventListener("click", evt -> search.open());

        return this;
    }

    public Tree enableFolding() {
        Icon collapseAll = Style.of(Icons.ALL.fullscreen_exit())
                .setMarginBottom("0px")
                .setMarginTop("0px")
                .css(Styles.pull_right)
                .setProperty("cursor", "pointer")
                .get()
                .setColor(Color.GREY);

        collapseAll.asElement().addEventListener("click", evt -> getSubItems().forEach(TreeItem::collapseAll));


        Icon expandAll = Style.of(Icons.ALL.fullscreen())
                .setMarginBottom("0px")
                .setMarginTop("0px")
                .css(Styles.pull_right)
                .setProperty("cursor", "pointer")
                .get()
                .setColor(Color.GREY);

        expandAll.asElement().addEventListener("click", evt -> getSubItems().forEach(TreeItem::expandAll));

        header.appendChild(expandAll.asElement());
        header.appendChild(collapseAll.asElement());
        return this;
    }

    public Tree autoExpandFound() {
        this.autoExpandFound = true;
        return this;
    }

    @Override
    public boolean isAutoExpandFound() {
        return autoExpandFound;
    }

    public void setAutoExpandFound(boolean autoExpandFound) {
        this.autoExpandFound = autoExpandFound;
    }

    public void clearFilter() {
        subItems.forEach(TreeItem::clearFilter);
    }

    public void filter(String searchToken) {
        subItems.forEach(treeItem -> treeItem.filter(searchToken));
    }

    @Override
    public Tree getTreeRoot() {
        return this;
    }

    public Tree setAutoCollapse(boolean autoCollapse) {
        this.autoCollapse = autoCollapse;
        return this;
    }

    public boolean isAutoCollapse() {
        return autoCollapse;
    }

    public List<TreeItem> getSubItems() {
        return subItems;
    }
}
