package co4;

class FlowEdge {
    int to, rev, cap;

    FlowEdge(int to, int rev, int cap) {
        this.to = to;
        this.rev = rev;
        this.cap = cap;
    }
}
