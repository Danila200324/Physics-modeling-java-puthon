package com.danven.gameOfLife;


import com.danven.gameOfLife.enums.SizeMode;
import com.danven.gameOfLife.enums.Axis;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class LayoutLinear implements LayoutManager2 {

    public static class ElementSpec {
        public SizeMode sizeMode;
        public int size;
        public int weight;
        public int minSecondarySize;

        public static ElementSpec createMin(int size) {
            ElementSpec spec = new ElementSpec();
            spec.sizeMode = SizeMode.SIZEMODE_MIN;
            spec.size = size;
            spec.minSecondarySize = 0;
            return spec;
        }

        public static ElementSpec createGrow(int weight) {
            ElementSpec spec = new ElementSpec();
            spec.sizeMode = SizeMode.SIZEMODE_GROW;
            spec.weight = weight;
            spec.minSecondarySize = 0;
            return spec;
        }
    }

    private static class Pass0Data {
        public ElementSpec[] elementSpecs;
        public Dimension[] childBaseDimensions;
        public Insets parentInsets;
        public Rectangle rcParentInset;
        public int[] childComputedSizes;
        public int fixedSizeSum;
        public int weightSum;
        public int crossMinSize;
    }

    private final Axis primaryAxis;
    private final Map<Component, ElementSpec> elementSpecs = new HashMap<>();

    public LayoutLinear(Axis primaryAxis) {
        this.primaryAxis = primaryAxis;
    }

    private void pass0(Container parent, Pass0Data p0d) {
        Component[] children = (Component[]) parent.getComponents();
        int numChildren = children.length;

        p0d.parentInsets = parent.getInsets();
        Dimension parentSize = parent.getSize();
        p0d.rcParentInset = new Rectangle(
                p0d.parentInsets.left,
                p0d.parentInsets.top,
                parentSize.width - p0d.parentInsets.right - p0d.parentInsets.left,
                parentSize.height - p0d.parentInsets.bottom - p0d.parentInsets.top
        );

        p0d.elementSpecs = new ElementSpec[numChildren];
        p0d.childBaseDimensions = new Dimension[numChildren];
        p0d.childComputedSizes = new int[numChildren];

        for (int i = 0; i < numChildren; i++) {
            Component child = children[i];
            p0d.childBaseDimensions[i] = child.getPreferredSize();
            p0d.elementSpecs[i] = elementSpecs.get(child);
        }

        int primInset = primaryAxis == Axis.AXIS_X ? (p0d.parentInsets.left + p0d.parentInsets.right) :
                        (p0d.parentInsets.top + p0d.parentInsets.bottom);
        int secInset = primaryAxis == Axis.AXIS_X ? (p0d.parentInsets.top + p0d.parentInsets.bottom) :
                       (p0d.parentInsets.left + p0d.parentInsets.right);

        p0d.fixedSizeSum = primInset;
        p0d.weightSum = 0;

        p0d.crossMinSize = secInset;

        for (int i = 0; i < numChildren; i++) {
            Dimension dim = p0d.childBaseDimensions[i];
            ElementSpec spec = p0d.elementSpecs[i];
            if (spec == null || spec.sizeMode == SizeMode.SIZEMODE_MIN) {
                int sz = spec != null ? spec.size : 0;
                int elSz = 0;
                if (primaryAxis == Axis.AXIS_X) {
                    elSz = dim.width;
                } else if (primaryAxis == Axis.AXIS_Y) {
                    elSz = dim.height;
                }
                if (sz < elSz) {
                    sz = elSz;
                }
                p0d.fixedSizeSum += sz;
                p0d.childComputedSizes[i] = sz;
            } else if (spec.sizeMode == SizeMode.SIZEMODE_FIXED) {
                p0d.fixedSizeSum += spec.size;
                p0d.childComputedSizes[i] = spec.size;
            } else if (spec.sizeMode == SizeMode.SIZEMODE_GROW) {
                p0d.weightSum += spec.weight;
                int elSz = 0;
                if (primaryAxis == Axis.AXIS_X) {
                    elSz = dim.width;
                } else if (primaryAxis == Axis.AXIS_Y) {
                    elSz = dim.height;
                }
                p0d.fixedSizeSum += elSz;
                p0d.childComputedSizes[i] = elSz;
            } else {
                throw new IllegalArgumentException("invalid sizeMode");
            }
            int elCrossSz = 0;
            if (primaryAxis == Axis.AXIS_X) {
                elCrossSz = dim.height;
            } else if (primaryAxis == Axis.AXIS_Y) {
                elCrossSz = dim.width;
            }
            elCrossSz += secInset;
            if (spec != null && elCrossSz < spec.minSecondarySize) {
                elCrossSz = spec.minSecondarySize;
            }
            if (p0d.crossMinSize < elCrossSz) {
                p0d.crossMinSize = elCrossSz;
            }
        }
    }

    @Override
    public void addLayoutComponent(Component comp, Object constraints) {
        elementSpecs.put(comp, constraints instanceof ElementSpec ? (ElementSpec) constraints : null);
    }

    @Override
    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    @Override
    public float getLayoutAlignmentX(Container target) {
        return 0;
    }

    @Override
    public float getLayoutAlignmentY(Container target) {
        return 0;
    }

    @Override
    public void invalidateLayout(Container target) {

    }

    @Override
    public void addLayoutComponent(String name, Component comp) {
        elementSpecs.put(comp, null);
    }

    @Override
    public void removeLayoutComponent(Component comp) {
        elementSpecs.remove(comp);
    }

    @Override
    public Dimension preferredLayoutSize(Container parent) {
        Pass0Data p0d = new Pass0Data();

        pass0(parent, p0d);

        if (primaryAxis == Axis.AXIS_X) {
            return new Dimension(p0d.fixedSizeSum, p0d.crossMinSize);
        } else if (primaryAxis == Axis.AXIS_Y) {
            return new Dimension(p0d.crossMinSize, p0d.fixedSizeSum);
        } else {
            throw new IllegalArgumentException("invalid primary axis");
        }
    }

    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }

    @Override
    public void layoutContainer(Container parent) {
        Pass0Data p0d = new Pass0Data();
        pass0(parent, p0d);

        Component[] children = (Component[]) parent.getComponents();
        int numChildren = children.length;

        int remainingSize = (primaryAxis == Axis.AXIS_X ? p0d.rcParentInset.width :
                             p0d.rcParentInset.height) - p0d.fixedSizeSum;

        for (int i = 0; i < numChildren; i++) {
            ElementSpec spec = p0d.elementSpecs[i];
            if (spec != null && spec.sizeMode == SizeMode.SIZEMODE_GROW) {
                p0d.childComputedSizes[i] += remainingSize * spec.weight / p0d.weightSum;
            }
        }

        int pos = 0;
        for (int i = 0; i < numChildren; i++) {
            Rectangle rc = new Rectangle();
            if (primaryAxis == Axis.AXIS_X) {
                rc.x = pos;
                rc.width = p0d.childComputedSizes[i];
                pos += rc.width;

                rc.y = 0;
                rc.height = p0d.rcParentInset.height;
            } else if (primaryAxis == Axis.AXIS_Y) {
                rc.y = pos;
                rc.height = p0d.childComputedSizes[i];
                pos += rc.height;

                rc.x = 0;
                rc.width = p0d.rcParentInset.width;
            } else {
                throw new IllegalArgumentException("invalid parameter");
            }

            Point p = new Point(rc.x, rc.y);
            p.x = p0d.parentInsets.left + p.x;
            p.y = p0d.parentInsets.top + p.y;

            children[i].setBounds(p.x, p.y, rc.width, rc.height);
        }
    }
}
