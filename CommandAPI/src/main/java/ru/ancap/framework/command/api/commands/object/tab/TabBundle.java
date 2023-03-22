package ru.ancap.framework.command.api.commands.object.tab;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PACKAGE) @With
@ToString @EqualsAndHashCode
public class TabBundle {
    
    private final List<TabCompletion> tabCompletions;
    private final int argumentsToReplace;
    private final boolean filter;
    
    public static TabBundle.Builder builder() {
        return new TabBundle.Builder();
    }
    
    public List<TabCompletion> tabCompletions() {
        return this.tabCompletions;
    }
    
    public int argumentsToReplace() {
        return this.argumentsToReplace;
    }
    
    public boolean filter() {
        return this.filter;
    }
    
    public static class Builder {
        
        private List<TabCompletion> tabCompletions;
        private int argumentsToReplace = 1;
        private boolean filter = true;
        
        public Builder description() {
            this.filter = false;
            return this;
        }
        
        public Builder replace(int toReplace) {
            this.argumentsToReplace = toReplace;
            return this;
        }
        
        public Builder raw(List<String> rawCompletions) {
            return this.tooltiped(rawCompletions.stream().map(Tab::new).collect(Collectors.toList()));
        }
        
        public Builder tooltiped(List<TabCompletion> completions) {
            this.tabCompletions = completions;
            return this;
        }
        
        public TabBundle build() {
            return new TabBundle(this.tabCompletions, this.argumentsToReplace, this.filter);
        }
        
    }
    
}
