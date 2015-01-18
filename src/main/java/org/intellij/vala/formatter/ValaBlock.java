package org.intellij.vala.formatter;

import com.intellij.formatting.*;
import com.intellij.formatting.templateLanguages.BlockWithParent;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.psi.formatter.FormatterUtil;
import com.intellij.psi.formatter.common.AbstractBlock;
import com.intellij.psi.tree.IElementType;
import org.intellij.vala.ValaLanguage;
import org.intellij.vala.psi.ValaTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ValaBlock extends AbstractBlock implements BlockWithParent {
    private final ValaIndentProcessor myIndentProcessor;
    private final ValaSpacingProcessor mySpacingProcessor;
    private final ValaWrappingProcessor myWrappingProcessor;
    private final ValaAlignmentProcessor myAlignmentProcessor;
    private final CodeStyleSettings mySettings;
    private Wrap myChildWrap = null;
    private final Indent myIndent;
    private boolean myChildrenBuilt = false;
    private BlockWithParent myParent;

    protected ValaBlock(ASTNode node,
                        Wrap wrap,
                        Alignment alignment,
                        CodeStyleSettings settings) {
        super(node, wrap, alignment);
        mySettings = settings;
        myIndentProcessor = new ValaIndentProcessor(mySettings.getCommonSettings(ValaLanguage.INSTANCE));
        mySpacingProcessor = new ValaSpacingProcessor(node, mySettings.getCommonSettings(ValaLanguage.INSTANCE));
        myWrappingProcessor = new ValaWrappingProcessor(node, mySettings.getCommonSettings(ValaLanguage.INSTANCE));
        myAlignmentProcessor = new ValaAlignmentProcessor(node, mySettings.getCommonSettings(ValaLanguage.INSTANCE));
        myIndent = myIndentProcessor.getChildIndent(myNode);
    }

    @Override
    public Indent getIndent() {
        return myIndent;
    }

    @Override
    public Spacing getSpacing(Block child1, @NotNull Block child2) {
        return mySpacingProcessor.getSpacing(child1, child2);
    }

    @Override
    protected List<Block> buildChildren() {
        myChildrenBuilt = true;
        if (isLeaf()) {
            return EMPTY;
        }
        final ArrayList<Block> tlChildren = new ArrayList<Block>();
        for (ASTNode childNode = getNode().getFirstChildNode(); childNode != null; childNode = childNode.getTreeNext()) {
            if (FormatterUtil.containsWhiteSpacesOnly(childNode)) continue;
            final ValaBlock childBlock = new ValaBlock(childNode, createChildWrap(childNode), createChildAlignment(childNode), mySettings);
            childBlock.setParent(this);
            tlChildren.add(childBlock);
        }
        return tlChildren;
    }

    public Wrap createChildWrap(ASTNode child) {
        final IElementType childType = child.getElementType();
        final Wrap wrap = myWrappingProcessor.createChildWrap(child, Wrap.createWrap(WrapType.NONE, false), myChildWrap);

        if (childType == ValaTypes.ASSIGNMENT_OPERATOR) {
            myChildWrap = wrap;
        }
        return wrap;
    }

    @Nullable
    protected Alignment createChildAlignment(ASTNode child) {
        if (child.getElementType() != ValaTypes.LEFT_PAREN && child.getElementType() != ValaTypes.BLOCK) {
            return myAlignmentProcessor.createChildAlignment();
        }
        return null;
    }

    @NotNull
    @Override
    public ChildAttributes getChildAttributes(final int newIndex) {
        int index = newIndex;
        ASTBlock prev = null;
        do {
            if (index == 0) {
                break;
            }
            prev = (ASTBlock) getSubBlocks().get(index - 1);
            index--;
        }
        while (prev.getNode().getElementType() == ValaTypes.SEMICOLON || prev.getNode() instanceof PsiWhiteSpace);

        final IElementType elementType = myNode.getElementType();
        final IElementType prevType = prev == null ? null : prev.getNode().getElementType();
        if (prevType == ValaTypes.LEFT_CURLY) {
            return new ChildAttributes(Indent.getNormalIndent(), null);
        }
        if (isEndsWithRPAREN(elementType, prevType)) {
            return new ChildAttributes(Indent.getNormalIndent(), null);
        }
        if (index == 0) {
            return new ChildAttributes(Indent.getNoneIndent(), null);
        }
        return new ChildAttributes(prev.getIndent(), prev.getAlignment());
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    private static boolean isEndsWithRPAREN(IElementType elementType, IElementType prevType) {
        return prevType == ValaTypes.RIGHT_PAREN &&
                (elementType == ValaTypes.IF_STATEMENT ||
                        elementType == ValaTypes.FOR_STATEMENT ||
                        elementType == ValaTypes.WHILE_STATEMENT);
    }

    @Override
    public BlockWithParent getParent() {
        return myParent;
    }

    @Override
    public void setParent(BlockWithParent newParent) {
        myParent = newParent;
    }
}
