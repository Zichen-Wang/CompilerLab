package spiglet.spiglet2kanga;

public class Section {
    public int tempID, start, end;
    public boolean hasCrossedCall;

    public Section(int tempID_, int start_, int end_) {
        tempID = tempID_;
        start = start_;
        end = end_;
        hasCrossedCall = false;
    }

    //按照start从小到大排序
    public int compareAsStart(Section compareSection) {
        return this.start - compareSection.start;
    }

    //按照end从小到大排序
    public int compareAsEnd(Section compareSection) {
        return this.end - compareSection.end;
    }

}
