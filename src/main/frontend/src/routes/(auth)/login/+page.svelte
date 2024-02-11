<script lang="ts">
    import Card from "$lib/components/Card.svelte";
    import Input from "$lib/components/Input.svelte";
    import Button from "$lib/components/Button.svelte";
    import { enhance } from '$app/forms';

    export let form;
</script>

<svelte:head>
    <title>Login - EM</title>
</svelte:head>

<Card
        title="Log In">
    <span slot="content">

        {#if !form?.success && form?.error.message != null }
            <p class="text-red-500">{form.error.message}</p>
        {/if}

        <form method="POST" use:enhance>
            <Input
                    type="email"
                    label="Email"
                    name="email"
                    placeholder="email"
                    required
                    autofocus
                    autocomplete="username"
            />

            <Input
                    type="password"
                    label="Password"
                    name="password"
                    placeholder="password"
                    required
                    autocomplete="current-password"
            />

            <!--            <Checkbox name="remember" label="Remember me"/>-->

            <div class="flex items-center justify-end mt-4">

                    <a href="/resetPassword" class="link link-hover">
                        Reset password
                    </a>

                    <Button class="ml-4" type="submit" disabled={false}>
                        LOG IN
                    </Button>
            </div>
        </form>

    </span>

</Card>