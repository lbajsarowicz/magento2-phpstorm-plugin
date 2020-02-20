/**
 * Copyright © Dmytro Kvashnin. All rights reserved.
 * See COPYING.txt for license details.
 */
package com.magento.idea.magento2plugin.indexes;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlAttributeValue;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.indexing.FileBasedIndex;
import com.magento.idea.magento2plugin.stubs.indexes.EntityNameIndex;
import com.magento.idea.magento2plugin.xml.XmlPsiTreeUtil;

import java.util.ArrayList;
import java.util.Collection;

public class EntityIndex {

    private static EntityIndex INSTANCE;

    private Project project;

    private EntityIndex() {
    }

    public static EntityIndex getInstance(final Project project) {
        if (null == INSTANCE) {
            INSTANCE = new EntityIndex();
        }
        INSTANCE.project = project;

        return INSTANCE;
    }

    public Collection<PsiElement> getEntityElements(final String name, final GlobalSearchScope scope) {
        Collection<PsiElement> result = new ArrayList<>();

        Collection<VirtualFile> virtualFiles =
                FileBasedIndex.getInstance().getContainingFiles(EntityNameIndex.KEY, name, scope);

        for (VirtualFile virtualFile : virtualFiles) {
            XmlFile xmlFile = (XmlFile) PsiManager.getInstance(project).findFile(virtualFile);
            Collection<XmlAttributeValue> valueElements = XmlPsiTreeUtil
                    .findAttributeValueElements(xmlFile, "entity", "name", name);
            result.addAll(valueElements);
        }
        return result;
    }
}
